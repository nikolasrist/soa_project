package opencrx

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpStatusCode
import opencrx.models.*

val accountUrl =
    "http://sepp-crm.inf.h-brs.de/opencrx-rest-CRX/org.opencrx.kernel.account1/provider/CRX/segment/Standard/account/"


val client = HttpClient(Apache) {
    install(JsonFeature) {
        serializer = JacksonSerializer()
    }
    install(Auth) {
        basic {
            username = "guest"
            password = "guest"
        }
    }
}

suspend fun getAccount(id: String): Response {
    val accountResponse = client.get<HttpResponse>(accountUrl + id)
    if (accountResponse.status == HttpStatusCode.OK) {
        return AccountResponse(accountResponse.receive())
    }
    return ErrorResponse("Failed to retrieve account: $id", accountResponse.status)
}

suspend fun getAssignedContract(assignedContractsUrl: String): Response {
    val contractListResponse = client.get<HttpResponse>("$assignedContractsUrl/assignedContract")
    if (contractListResponse.status == HttpStatusCode.OK) {
        return ContractListResponse(contractListResponse.receive())
    }
    return ErrorResponse("Failed to retrieve assigned contract list.", contractListResponse.status)
}
suspend fun getSalesOrderPosition(salesOrderUrl: String): Response {
    val salesOrderPositionListResponse = client.get<HttpResponse>("$salesOrderUrl/position")
    if (salesOrderPositionListResponse.status == HttpStatusCode.OK) {
        return SalesOrderPositionListResponse(salesOrderPositionListResponse.receive())
    }
    return ErrorResponse("Failed to retrieve sales order position list.", salesOrderPositionListResponse.status)
}

suspend fun getAllAccounts(): Response {
    val accountListResponse = client.get<HttpResponse>(accountUrl)
    if (accountListResponse.status == HttpStatusCode.OK) {
        return AccountListResponse(accountListResponse.receive())
    }
    return ErrorResponse("Failed to retrieve account list.", accountListResponse.status)
}