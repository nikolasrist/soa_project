package orangeHRM

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.HttpStatusCode
import io.ktor.http.parametersOf
import orangeHRM.models.*
import java.time.Instant


class OrangeCRMClient {
//    val baseUrl = "https://sepp-hrm.inf.h-brs.de"
    val baseUrl = "https://orangehrm.ironmanserver.de/"
    val apiBaseUrl = "/symfony/web/api/v1"
    val oauthApiBaseUrl = "/symfony/web/index.php"
    val oauthApi = "/oauth/issueToken"
    val userApi = "/user"
    val tokenPrefix = "Bearer "
    var token = ""
    var expiresTime: Instant = Instant.now()

    val clientId = "admin"
    val clientSecret = "admin"

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        defaultRequest {
            header("Authorization", "$tokenPrefix$token")
        }
    }


    suspend fun getAllAccounts(): Response {
        val httpResponse = client.get<HttpResponse>("$baseUrl$apiBaseUrl$userApi")
        if (httpResponse.status != HttpStatusCode.OK) {
            return ErrorResponse("Failed to retrieve account.", httpResponse.status)
        }
        return AccountListResponse(httpResponse.receive())
    }

    suspend fun getAccount(accountId: String): Response {
        val httpResponse = client.get<HttpResponse>("$apiBaseUrl$userApi/$accountId")
        if (httpResponse.status != HttpStatusCode.OK) {
            return ErrorResponse("Failed to retrieve account.", httpResponse.status)
        }
        return AccountResponse(httpResponse.receive())
    }

    suspend fun getOrganization(): String {
        val httpResponse = client.get<HttpResponse>()
    }


    suspend fun setToken() {
        println("check token refresh")
        if (Instant.now() > expiresTime) {
            println("try to refresh token")
            val requestUrl = "$baseUrl$oauthApiBaseUrl$oauthApi"
            println("Request URL: $requestUrl")
            var data = parametersOf(
                Pair("client_id", listOf(clientId)),
                Pair("client_secret", listOf(clientSecret)),
                Pair("grant_type", listOf("client_credentials"))
            )
            println(data)
            val response = client.submitForm<HttpResponse>(url = requestUrl, formParameters = data)
            val responseText = response.readText()
            val mapper = jacksonObjectMapper()
            val readValue = mapper.readValue<TokenResponse>(responseText)
            expiresTime = Instant.now().plusMillis(readValue.expiresIn)
            token = readValue.accessToken
            println("Token: $token \nExpiresIn: $expiresTime")
        }
    }
}
