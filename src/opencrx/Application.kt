package opencrx

import kotlinx.coroutines.runBlocking
import opencrx.models.*
import opencrx.utils.printAccount
import opencrx.utils.printAccountList
import opencrx.utils.printError

val accountId = "RC9375R5ALYW9H84KQGL5CLHT"

fun main() = runBlocking {
    println("Start client")
    println("Get Account for ID: " + accountId)
    val accountResponse = getAccount(accountId)
    when (accountResponse) {
        is AccountResponse -> printAccount(accountResponse.data)
        is ErrorResponse -> printError(accountResponse)
    }

    println("Get all accounts:")
    val accountListResponse = getAllAccounts()
    when (accountListResponse) {
        is AccountListResponse -> printAccountList(accountListResponse.data)
        is ErrorResponse -> printError(accountListResponse)
    }
}
