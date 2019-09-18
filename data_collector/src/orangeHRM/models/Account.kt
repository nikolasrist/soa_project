package orangeHRM.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class Account(
    val userName: String,
    val userRole: String,
    val status: String,
    val employeeName: String,
    val employeeId: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountList(
    val data: List<Account>
)