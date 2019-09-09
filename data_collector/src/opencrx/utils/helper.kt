package opencrx.utils

import opencrx.models.*


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
    println("Industry: " + account.industry)
    println("AccountRating: " + account.accountRating)
    println("AccountURL: " + account.accountUrl)
    println("-------------------")
}

fun printContractList(contractList: ContractList) {
    for (contract in contractList.objects) {
        printContract(contract)
    }
}

fun printContract(contract: AssignedContract) {
    println("Type: " + contract.type)
    println("salesOrderURL: " + contract.salesOrderUrl)
    println("-------------------")
}

fun printSalesOrderPositionList(salesOrderPositionList: SalesOrderPositionList) {
    for (salesOrderPosition in salesOrderPositionList.objects) {
        printSalesOrderPosition(salesOrderPosition)
    }
}

fun printSalesOrderPosition(salesOrderPosition: SalesOrderPosition) {
    println("baseAmount: " + salesOrderPosition.baseAmount)
    println("Quantity: " + salesOrderPosition.quantity)
    println("-------------------")
}