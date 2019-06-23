package orangeHRM

import kotlinx.coroutines.runBlocking
import orangeHRM.models.*


fun main() = runBlocking {
    println("Start client")
    val oHRMClient = OrangeCRMClient()
    oHRMClient.setToken()
//    val accountId = "0001"
//    println("Get Account for ID: " + accountId)
//    val accountResponse = oHRMClient.getAccount(accountId)
//    when (accountResponse) {
//        is AccountResponse -> printAccount(accountResponse.data)
//        is ErrorResponse -> printError(accountResponse)
//    }

    println("Get all accounts:")
    val accountListResponse = oHRMClient.getAllAccounts()
    when (accountListResponse) {
        is AccountListResponse -> printAccountList(accountListResponse.data)
        is ErrorResponse -> printError(accountListResponse)
    }
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

