package opencrx.utils

import opencrx.models.Account
import opencrx.models.AccountList
import opencrx.models.ErrorResponse


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