package opencrx

import kotlinx.coroutines.runBlocking
import opencrx.models.*
import opencrx.utils.*

val accountId = "RC9375R5ALYW9H84KQGL5CLHT"

fun main() = runBlocking {
    println("Start client")
    println("Get Account for ID: " + accountId)
    when (val accountResponse = getAccount(accountId)) {
        is AccountResponse -> printAccount(accountResponse.data)
        is ErrorResponse -> printError(accountResponse)
    }

    println("Get all accounts:")
    when (val accountListResponse = getAllAccounts()) {
        is AccountListResponse -> {
            printAccountList(accountListResponse.data)
            for (accountResponse in accountListResponse.data.objects.filter { it.industry != "" }) {
                println("Get all contracts:")
                when (val contractListResponse = accountResponse.accountUrl?.let { getAssignedContract(it) }) {
                    is ContractListResponse -> {
                        printContractList(contractListResponse.data)
                        for (contractResponse in contractListResponse.data.objects) {
                            when ( val salesOrderPositionListResponse =
                                contractResponse.salesOrderUrl?.let { getSalesOrderPosition(it) }){
                                is SalesOrderPositionListResponse -> {
                                    printSalesOrderPositionList(salesOrderPositionListResponse.data)
                                }
                                is ErrorResponse -> printError(salesOrderPositionListResponse)
                            }
                        }
                    }
                    is ErrorResponse -> printError(contractListResponse)

                }
            }

        }
        is ErrorResponse -> printError(accountListResponse)
    }


}
