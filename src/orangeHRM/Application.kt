package orangeHRM

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import orangeHRM.models.*
import orangeHRM.utils.printAccountList
import orangeHRM.utils.printError
import orangeHRM.utils.printOrganization


fun main() = runBlocking {
    println("Start client")
    val oHRMClient = OrangeCRMClient()
    oHRMClient.setToken()

    println("Get all accounts:")
    val accountListResponse = oHRMClient.getAllAccounts()
    when (accountListResponse) {
        is AccountListResponse -> printAccountList(accountListResponse.data)
        is ErrorResponse -> printError(accountListResponse)
    }

    println("Get Organization:")
    val organizationResponse = oHRMClient.getOrganization()
    when (organizationResponse) {
        is OrganizationResponse -> printOrganization(organizationResponse.data)
        is ErrorResponse -> printError(organizationResponse)
    }
}
