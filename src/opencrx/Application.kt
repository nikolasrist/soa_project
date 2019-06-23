package opencrx

import kotlinx.coroutines.runBlocking
import opencrx.models.*

val accountUrl =
    "http://sepp-crm.inf.h-brs.de/opencrx-rest-CRX/org.opencrx.kernel.account1/provider/CRX/segment/Standard/account/"
val accountId = "RC9375R5ALYW9H84KQGL5CLHT"


fun main() = runBlocking {
    println("Start client")
    println("Get Account for ID: " + accountId)
    val accountResponse = getAccount(accountUrl, accountId)
    when (accountResponse) {
        is AccountResponse -> printAccount(accountResponse.data)
        is ErrorResponse -> printError(accountResponse)
    }

    println("Get all accounts:")
    val accountListResponse = getAllAccounts(accountUrl)
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
    for (acc in accountList.objects) {
        printAccount(acc)
    }
}

fun printAccount(account: Account) {
    println("Account Infos:")
    println("FullName: " + account.fullName)
    println("JobTitle: " + account.jobTitle)
    println("Organization: " + account.organization)
    println("-------------------")
}