package controller.open.order

import kotlinx.serialization.Serializable
import logic.PlatformAuthLogic
import logic.ChargeLogic
import model.ChargeRecord
import table.ChargeRecordTable
import neton.core.annotations.AllowAnonymous
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Post
import neton.core.annotations.Body
import neton.database.dsl.*
import kotlin.time.Clock

@Serializable
data class OrderQueryRequest(
    val appId: String,
    val orderId: String? = null,
    val timestamp: String,
    val signature: String
)

@Serializable
data class OrderCreateRequest(
    val appId: String,
    val productCode: String,
    val quantity: Int = 1,
    val timestamp: String,
    val signature: String
)

@Serializable
data class OrderQueryResponse(
    val code: Int = 0,
    val message: String = "success",
    val orderId: String? = null,
    val status: String? = null,
    val amount: Long? = null
)

@Serializable
data class OrderCreateResponse(
    val code: Int = 0,
    val message: String = "success",
    val orderId: String? = null
)

@Controller("/order")
class PlatformOrderController(
    private val platformAuthLogic: PlatformAuthLogic,
    private val chargeLogic: ChargeLogic? = null
) {

    @Get("/query")
    @AllowAnonymous
    suspend fun query(
        appId: String,
        orderId: String? = null,
        timestamp: String,
        signature: String
    ): OrderQueryResponse {
        val client = platformAuthLogic.lookupClient(appId)
            ?: return OrderQueryResponse(code = 401, message = "Invalid appId")

        val stringToSign = buildString {
            append("appId=$appId")
            orderId?.let { append("&orderId=$it") }
            append("&timestamp=$timestamp")
        }

        if (!platformAuthLogic.verifySignature(client.appSecret, stringToSign, signature)) {
            return OrderQueryResponse(code = 403, message = "Signature verification failed")
        }

        // Query charge records for this client and order
        if (orderId != null) {
            val record = ChargeRecordTable.oneWhere {
                and(
                    ChargeRecord::clientId eq client.id,
                    ChargeRecord::orderId eq orderId
                )
            }

            if (record != null) {
                return OrderQueryResponse(
                    code = 0,
                    message = "success",
                    orderId = record.orderId,
                    status = if (record.status == 0) "pending" else "completed",
                    amount = record.amount
                )
            }
        }

        return OrderQueryResponse(
            code = 404,
            message = "Order not found",
            orderId = orderId
        )
    }

    @Post("/create")
    @AllowAnonymous
    suspend fun create(@Body request: OrderCreateRequest): OrderCreateResponse {
        val client = platformAuthLogic.lookupClient(request.appId)
            ?: return OrderCreateResponse(code = 401, message = "Invalid appId")

        val stringToSign = buildString {
            append("appId=${request.appId}")
            append("&productCode=${request.productCode}")
            append("&quantity=${request.quantity}")
            append("&timestamp=${request.timestamp}")
        }

        if (!platformAuthLogic.verifySignature(client.appSecret, stringToSign, request.signature)) {
            return OrderCreateResponse(code = 403, message = "Signature verification failed")
        }

        // Create a charge record for this API call
        val orderId = "ORD_${client.appId}_${Clock.System.now().toEpochMilliseconds()}"
        val chargeRecord = ChargeRecord(
            clientId = client.id,
            apiId = 0,  // Will be resolved by API code lookup
            orderId = orderId,
            apiCode = request.productCode,
            amount = 0,  // Will be calculated based on API pricing
            status = 0
        )
        ChargeRecordTable.insert(chargeRecord)

        return OrderCreateResponse(
            code = 0,
            message = "success",
            orderId = orderId
        )
    }
}
