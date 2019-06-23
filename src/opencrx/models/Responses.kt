package opencrx.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.ktor.http.HttpStatusCode

sealed class Response

@JsonIgnoreProperties(ignoreUnknown = true)
data class Account(
    val firstName: String? = "",
    val lastName: String? = "",
    val fullName: String? = "",
    val familyStatus: Int = 0,
    val organization: String? = "",
    val jobTitle: String? = "",
    val gender: Int = 0,
    val preferredSpokenLanguage: Int = 0,
    val accountRating: Int = 0,
    val annualIncomeCurrency: Int = 0
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountList(
    val objects: List<Account>
)

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

