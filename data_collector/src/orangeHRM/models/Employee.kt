package orangeHRM.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class Employee(
    val firstName: String,
    val lastName: String,
    val employeeId: String,
    val jobTitle: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EmployeeList(
    val data: List<Employee>
)