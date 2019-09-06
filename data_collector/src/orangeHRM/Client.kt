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


class OrangeHRMClient {
    val baseUrl = "https://sepp-hrm.inf.h-brs.de" // for hbrs server
    val apiBaseUrl = "/symfony/web/index.php/api/v1"
    val oauthApiBaseUrl = "/symfony/web/index.php"
    val oauthApi = "/oauth/issueToken"
    val userApi = "/user"
    val employeesApi = "/employee/search"
    val organizationApi = "/organization"
    val tokenPrefix = "Bearer "
    var token = ""
    var expiresTime: Instant = Instant.now()

    // hbrs server credentials
    val clientId = "tom"
    val clientSecret = "tom123"

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        defaultRequest {
            header("Authorization", "$tokenPrefix$token")
        }
    }


    suspend fun getAllEmployees(): Response{
        val httpResponse = client.get<HttpResponse>("$baseUrl$apiBaseUrl$employeesApi")
        if (httpResponse.status != HttpStatusCode.OK) {
            return ErrorResponse("Failed to retrieve employees.", httpResponse.status)
        }
        return EmployeeListResponse(httpResponse.receive())
    }

    suspend fun getEmployee(name: String): Response{
        val httpResponse = client.get<HttpResponse>("$baseUrl$apiBaseUrl$employeesApi?name=$name")
        if (httpResponse.status != HttpStatusCode.OK) {
            return ErrorResponse("Failed to retrieve employees.", httpResponse.status)
        }
        return EmployeeListResponse(httpResponse.receive())
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

    suspend fun getOrganization(): Response {
        val httpResponse = client.get<HttpResponse>("$baseUrl$apiBaseUrl$organizationApi")
        if (httpResponse.status != HttpStatusCode.OK) {
            return ErrorResponse("Failed to retrieve organization.", httpResponse.status)
        }
        return httpResponse.receive<OrganizationResponse>()
    }


    suspend fun setToken() {
        println("set token")
        if (Instant.now() > expiresTime) {
            val requestUrl = "$baseUrl$oauthApiBaseUrl$oauthApi"
            var data = parametersOf(
                Pair("client_id", listOf(clientId)),
                Pair("client_secret", listOf(clientSecret)),
                Pair("grant_type", listOf("client_credentials"))
            )
            val response = client.submitForm<HttpResponse>(url = requestUrl, formParameters = data)
            val responseText = response.readText()
            val mapper = jacksonObjectMapper()
            val readValue = mapper.readValue<TokenResponse>(responseText)
            expiresTime = Instant.now().plusMillis(readValue.expiresIn)
            token = readValue.accessToken
            println("Token: $token \nExpiresTime: $expiresTime")
        }
    }
}
