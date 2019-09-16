package opencrx

import kotlinx.coroutines.runBlocking
import opencrx.models.*
import opencrx.utils.printError

val accountId = "RC9375R5ALYW9H84KQGL5CLHT"

fun main() = runBlocking {
    println("Start client")
    println("Get Account for ID: " + accountId)
    val salesManName = "John Smith"
    collectSalesManInformation(salesManName)
}

suspend fun collectSalesManInformation(salesManName: String): ClientInfoDTO {
    val salesMan = getSalesmanInformation(salesManName)
    val clientInfoDTO = ClientInfoDTO(salesmanName = salesManName)
    clientInfoDTO.salesInfos = ArrayList()
    val assignedContractsDTO = retrieveAssignedContracts(salesMan)
    val contractMap: HashMap<String, HashMap<Account, SalesOrderPositionList>> = HashMap()
    loop@ for (contract in assignedContractsDTO.objects) {
        val tempMap = HashMap<Account, SalesOrderPositionList>()
        val account = getAccountByUrl(contract.customer.customerUrl!!)
        when (account) {
            is AccountResponse -> tempMap.put(account.data, retrieveSalesOrderPositions(contract))
            else -> continue@loop
        }
        contractMap.put(contract.salesOrderUrl!!, tempMap)
    }

    for (positionsKey in contractMap.keys) {
        val accountPositionsMap = contractMap[positionsKey]
        for (account in accountPositionsMap!!.keys) {
            val positions = accountPositionsMap[account]
            for (position in positions!!.objects) {
                val productInformation = retrieveProductInformation(position)
                val salesInfoDTO = SalesInfoDTO()
                salesInfoDTO.clientName = account.fullName.toString()
                salesInfoDTO.clientRanking = account.accountRating.toString().toInt()
                salesInfoDTO.productName = productInformation.name.toString()
                salesInfoDTO.quantity = position.quantity.toDouble()
                clientInfoDTO.salesInfos.add(salesInfoDTO)
            }
        }
    }
    println("ContractMap:")
    println(contractMap)
    println("=================")
    println("=================")
    println("ClientInfoDTO:")
    println(clientInfoDTO)
    return clientInfoDTO
}

suspend fun getSalesmanInformation(salesManName: String): Account {
    var clientInfoDTO = ClientInfoDTO()
    clientInfoDTO.salesmanName = salesManName
    var salesMan: Account
    when (val accountListResponse = getAllAccounts()) {
        is AccountListResponse -> {
            salesMan = filterSalesManAccount(accountListResponse.data, salesManName)
        }
        else -> salesMan = Account()
    }
    return salesMan
}

fun filterSalesManAccount(accountList: AccountList, salesManName: String): Account {
    val split = salesManName.split(" ")
    val firstName = split.get(0)
    val lastName = split.get(1)
    println("FirstName: $firstName")
    println("LastName: $lastName")
    return accountList.objects.filter { it.firstName == firstName && it.lastName == lastName }.first()
}


private suspend fun retrieveAssignedContracts(
    industryAccount: Account
): ContractList {
    when (val contractListResponse = industryAccount.accountUrl?.let { getAssignedContract(it) }) {
        is ContractListResponse -> {
            return contractListResponse.data
        }
        is ErrorResponse -> printError(contractListResponse)
    }
    return ContractList(emptyList())
}

private suspend fun retrieveSalesOrderPositions(assignedContract: AssignedContract): SalesOrderPositionList {
    when (val salesOrderPositionListResponse =
        assignedContract.salesOrderUrl?.let { getSalesOrderPosition(it) }) {
        is SalesOrderPositionListResponse -> {
            return salesOrderPositionListResponse.data
        }
        is ErrorResponse -> {
            printError(salesOrderPositionListResponse)
            return SalesOrderPositionList(emptyList())
        }
    }
    return SalesOrderPositionList(emptyList())
}


private suspend fun retrieveProductInformation(salesOrder: SalesOrderPosition): Product {
    when (val productResponse = salesOrder.product.reference?.let { getProduct(it) }) {
        is ProductResponse -> return productResponse.data
        is ErrorResponse -> return Product()
    }
    return Product()
}
