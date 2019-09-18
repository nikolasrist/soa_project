package orangeHRM.models

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpStatusCode
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class TokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
    @JsonProperty("token_type")    val tokenType: String
)

sealed class Response

data class BaseResponse(
    val data: HttpResponse
) : Response()

@JsonIgnoreProperties(ignoreUnknown = true)
data class EmployeeListResponse(
    val data: EmployeeList
) : Response()

data class AccountResponse(
    val data: Account
) : Response()

@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountListResponse(
    val data: AccountList
) : Response()

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrganizationResponse(
    val data: Organization
) : Response()

data class ErrorResponse(
    val message: String,
    val status: HttpStatusCode
) : Response()