package orangeHRM

import kotlinx.coroutines.runBlocking
import orangeHRM.models.*
import orangeHRM.utils.printAccountList
import orangeHRM.utils.printEmployeeList
import orangeHRM.utils.printError


fun main() = runBlocking {
    println("Start client")
    val oHRMClient = OrangeHRMClient()
    oHRMClient.setToken()

//    println("Get all accounts:")
//    when (val accountListResponse = oHRMClient.getAllAccounts()) {
//        is AccountListResponse -> printAccountList(accountListResponse.data)
//        is ErrorResponse -> printError(accountListResponse)
//    }
//
//    println("Get all employees:")
//    when (val employeeListResponse = oHRMClient.getAllEmployees()) {
//        is EmployeeListResponse -> printEmployeeList(employeeListResponse.data)
//        is ErrorResponse -> printError(employeeListResponse)
//    }

    println("Add salary bonus")
    when (val httpResponse = oHRMClient.addBonusSalary("7", "8","4000")) {
        is BaseResponse -> println(httpResponse.data)
        is ErrorResponse -> printError(httpResponse)
    }

}
