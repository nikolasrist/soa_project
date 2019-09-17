package core

import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.document
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.*
import io.ktor.server.engine.*
import opencrx.collectSalesManInformation
import opencrx.models.ClientInfoDTO
import orangeHRM.OrangeHRMClient
import orangeHRM.models.Employee
import orangeHRM.models.EmployeeListResponse
import orangeHRM.models.ErrorResponse
import orangeHRM.utils.printEmployee
import orangeHRM.utils.printError
import org.slf4j.event.Level

fun main(args: Array<String>) {
    embeddedServer(Netty, 9050) {
        install(ContentNegotiation){
            jackson{}
        }
        install(CallLogging) {
            level = Level.DEBUG
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
                get("salesman/{name}/bonusInfo") {
                    val name = call.parameters["name"]
                    call.respond(collectSalesManInformation(name!!))
                }
                put("salesman/{name}/bonusInfo") {
                    val updatedDTO = call.receive<ClientInfoDTO>()
                    println("UPDATED DTO received:")
                    println(updatedDTO)

                    call.respond(HttpStatusCode.OK, "Successful updated.")
                }
            }
        }
    }.start(wait = true)
}

suspend fun updateSalesman(clientInfoDTO: ClientInfoDTO) : HttpStatusCode {
    val oHRMClient = OrangeHRMClient()
    oHRMClient.setToken()
    //todo implement if needed
    return HttpStatusCode.OK
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