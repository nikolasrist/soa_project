package core

import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.server.engine.*
import orangeHRM.OrangeHRMClient
import orangeHRM.models.Employee
import orangeHRM.models.EmployeeListResponse
import orangeHRM.models.EmployeeResponse
import orangeHRM.models.ErrorResponse
import orangeHRM.utils.printEmployee
import orangeHRM.utils.printError

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation){
            jackson{}
        }
        install(StatusPages) {
            exception<Throwable> { cause ->
                println(cause)
                call.respond(HttpStatusCode.InternalServerError)
            }
            exception<NotFoundException> { cause ->
                call.respond(HttpStatusCode.NotFound)
            }
        }
        routing {
            route("/") {
                get() {
                    call.respond(getSalesmen())
                }
                get("salesman/{name}") {
                    val name = call.parameters["name"]
                    call.respond(getSalesman(name!!))
                }
            }
        }
    }.start(wait = true)
}

suspend fun getSalesmen(): List<Employee> {
    val oHRMClient = OrangeHRMClient()
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

suspend fun getSalesman (employeeName: String): Employee {
    val oHRMClient = OrangeHRMClient()
    oHRMClient.setToken()
    return when (val employeeResponse = oHRMClient.getEmployee(employeeName)) {
        is EmployeeListResponse -> {
            employeeResponse.data.data[0]
        }
        is ErrorResponse -> {
            println("Error returned.")
            printError(employeeResponse)
            throw NotFoundException()
        }
        else -> throw IllegalArgumentException()
    }
}