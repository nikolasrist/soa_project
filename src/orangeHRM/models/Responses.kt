package orangeHRM.models

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.http.HttpStatusCode
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Account(
    val userName: String,
    val userRole: String,
    val status: String,
    val employeeName: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountList(
    val data: List<Account>
)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class TokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
    @JsonProperty("token_type")    val tokenType: String
)

sealed class Response

data class AccountResponse(
    val data: Account
) : Response()

data class AccountListResponse(
    val data: AccountList
) : Response()

data class ErrorResponse(
    val message: String,
    val status: HttpStatusCode
) : Response()