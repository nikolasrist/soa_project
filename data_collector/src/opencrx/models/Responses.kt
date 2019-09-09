package opencrx.models

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
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
    val industry: String? = "",
    val annualIncomeCurrency: Int = 0,
    @JsonProperty("@href") val accountUrl: String? = ""
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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssignedContract(
    @JsonProperty("@type") val type: String? = "",
    @JsonProperty("@href") val salesOrderUrl: String? = ""
    )

@JsonIgnoreProperties(ignoreUnknown = true)
data class ContractList(
    val objects: List<AssignedContract>
)
data class ContractListResponse(
    val data: ContractList
) : Response()

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class SalesOrderPosition(
    val baseAmount: String,
    val quantity: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SalesOrderPositionList(
    val objects: List<SalesOrderPosition>
)
data class SalesOrderPositionListResponse(
    val data: SalesOrderPositionList
) : Response()

