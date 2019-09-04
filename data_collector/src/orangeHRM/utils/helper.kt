package orangeHRM.utils

import orangeHRM.models.*


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

fun printEmployeeList(employeeList: EmployeeList) {
    for (emp in employeeList.data) {
        printEmployee(emp)
    }
}

fun printEmployee(emp: Employee) {
    println("Employee Infos:")
    println("FirstName: " + emp.firstName)
    println("LastName: " + emp.lastName)
    println("JobTitle: " + emp.jobTitle)
    println("EmployeeId: " + emp.employeeId)
    println("-------------------")
}
