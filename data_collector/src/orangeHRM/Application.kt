package orangeHRM

import kotlinx.coroutines.runBlocking
import orangeHRM.models.*
import orangeHRM.utils.printAccountList
import orangeHRM.utils.printEmployeeList
import orangeHRM.utils.printError


fun main() = runBlocking {
    println("Start client")
    val oHRMClient = OrangeCRMClient()
    oHRMClient.setToken()

    println("Get all accounts:")
    when (val accountListResponse = oHRMClient.getAllAccounts()) {
        is AccountListResponse -> printAccountList(accountListResponse.data)
        is ErrorResponse -> printError(accountListResponse)
    }

    println("Get all employees:")
    when (val employeeListResponse = oHRMClient.getAllEmployees()) {
        is EmployeeListResponse -> printEmployeeList(employeeListResponse.data)
        is ErrorResponse -> printError(employeeListResponse)
    }


}
