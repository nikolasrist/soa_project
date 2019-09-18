package orangeHRM.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Organization(
    val id: String,
    val name: String,
    val email: String?,
    val country: String,
    val numberOfEmployees: String
)