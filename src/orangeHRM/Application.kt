package orangeHRM

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import orangeHRM.models.*


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

fun printOrganization(data: Organization) {
    println("Organization Infos: ")
    println("ID: ${data.id}")
    println("Name: ${data.name}")
    println("email: ${data.email}")
    println("country: ${data.country}")
    println("number of employees: ${data.numberOfEmployees}")
}

fun printError(responseError: ErrorResponse) {
    println(
        "Error occured. " +
                responseError.status.value + " " +
                responseError.status.description + " " +
                responseError.message
    )
}

fun printAccountList(accountList: AccountList) {
    for (acc in accountList.data) {
        printAccount(acc)
    }
}

fun printAccount(account: Account) {
    println("Account Infos:")
    println("FullName: " + account.employeeName)
    println("UserName: " + account.userName)
    println("Role: " + account.userRole)
    println("Status: " + account.status)
    println("-------------------")
}

