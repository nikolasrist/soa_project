package core

import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.server.engine.*
import orangeHRM.OrangeCRMClient
import orangeHRM.models.AccountListResponse
import orangeHRM.models.Employee
import orangeHRM.models.EmployeeListResponse
import orangeHRM.models.ErrorResponse
import orangeHRM.utils.printEmployeeList
import orangeHRM.utils.printError

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation){
            jackson{}
        }
        routing {
            get("/") {
                call.respond(getSalesmen())
            }
        }
    }.start(wait = true)
}

suspend fun getSalesmen(): List<Employee> {
    val oHRMClient = OrangeCRMClient()
    oHRMClient.setToken()
    return when (val employeeListResponse = oHRMClient.getAllEmployees()) {
        is EmployeeListResponse -> {
            employeeListResponse.data.data.filter { it.jobTitle == "Senior Salesman" }
        }
        is ErrorResponse -> {
            printError(employeeListResponse)
            return emptyList()
        }
        else -> return emptyList()
    }
}