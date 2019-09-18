package core

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.NotFoundException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import opencrx.collectSalesManInformation
import opencrx.models.ClientInfoDTO
import orangeHRM.OrangeHRMClient
import orangeHRM.models.BaseResponse
import orangeHRM.models.Employee
import orangeHRM.models.EmployeeListResponse
import orangeHRM.models.ErrorResponse
import orangeHRM.utils.printError
import org.slf4j.event.Level

fun main(args: Array<String>) {
    embeddedServer(Netty, 9050) {
        install(ContentNegotiation) {
            jackson {}
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
                    val name = call.parameters["name"]
                    val salesman = getSalesman(name!!)
                    val updatedDTO = call.receive<ClientInfoDTO>()
                    println("Employee to be updated:")
                    println(salesman)
                    println("UPDATED DTO received:")
                    println(updatedDTO)
                    call.respond(updateSalesmanBonusSalary(updatedDTO, salesman))
                }
            }
        }
    }.start(wait = true)
}

suspend fun updateSalesmanBonusSalary(clientInfoDTO: ClientInfoDTO, salesman: Employee): HttpStatusCode {
    val oHRMClient = OrangeHRMClient()
    oHRMClient.setToken()
    val bonus = clientInfoDTO.salesInfos.stream().mapToInt({it.bonus}).sum().toString()
    return when (val result = oHRMClient.addBonusSalary(salesman.employeeId, "8", bonus)) {
        is BaseResponse -> HttpStatusCode.OK
        is ErrorResponse -> {
            printError(result)
            return HttpStatusCode.InternalServerError
        }
        else -> HttpStatusCode.Conflict
    }
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

suspend fun getSalesman(employeeName: String): Employee {
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