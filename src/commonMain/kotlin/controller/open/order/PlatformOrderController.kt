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
import neton.core.annotations.RateLimit
import neton.core.annotations.RateLimitScope
import neton.core.http.HttpException
import neton.core.http.NetonErrorCode
import neton.core.http.ValidationError
import logic.PlatformErrorCodes
import kotlin.random.Random
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
    val orderId: String? = null,
    val status: String? = null,
    val amount: Long? = null
)

@Serializable
data class OrderCreateResponse(
    val orderId: String? = null
)

@Controller("/order")
class PlatformOrderController(
    private val platformAuthLogic: PlatformAuthLogic,
    private val chargeLogic: ChargeLogic? = null
) {

    @Get("/query")
    @AllowAnonymous
    @RateLimit(windowSeconds = 60, maxRequests = 60, scope = RateLimitScope.IP, message = PlatformErrorCodes.ORDER_QUERY_RATE_LIMITED)
    suspend fun query(
        appId: String,
        orderId: String? = null,
        timestamp: String,
        signature: String
    ): OrderQueryResponse {
        // Reject requests with stale or future timestamps (replay attack prevention)
        checkTimestamp(timestamp)
        val client = platformAuthLogic.lookupClient(appId) ?: throw appIdInvalid()

        // Parameters must be sorted alphabetically before signing to prevent ordering ambiguity.
        val params = mutableListOf("appId=$appId", "timestamp=$timestamp")
        orderId?.let { params.add("orderId=$it") }
        val stringToSign = params.sorted().joinToString("&")

        if (!platformAuthLogic.verifySignature(client.appSecret, stringToSign, signature)) {
            throw signatureInvalid()
        }

        val charge = chargeLogic ?: throw chargeServiceUnavailable()
        if (orderId == null) {
            throw HttpException(
                NetonErrorCode.MISSING_REQUIRED_PARAM,
                PlatformErrorCodes.ORDER_ID_REQUIRED,
                listOf(ValidationError("orderId", PlatformErrorCodes.ORDER_ID_REQUIRED, PlatformErrorCodes.ORDER_ID_REQUIRED))
            )
        }
        val record = charge.getChargeRecordByOrderId(orderId)
            ?: throw HttpException(NetonErrorCode.RESOURCE_NOT_FOUND, PlatformErrorCodes.ORDER_NOT_FOUND)
        return OrderQueryResponse(
            orderId = record.orderId,
            status = if (record.status == 1) "completed" else "pending",
            amount = record.amount
        )
    }

    @Post("/create")
    @AllowAnonymous
    @RateLimit(windowSeconds = 60, maxRequests = 30, scope = RateLimitScope.IP, message = PlatformErrorCodes.ORDER_CREATE_RATE_LIMITED)
    suspend fun create(@Body request: OrderCreateRequest): OrderCreateResponse {
        checkTimestamp(request.timestamp)
        val client = platformAuthLogic.lookupClient(request.appId) ?: throw appIdInvalid()

        // Parameters must be sorted alphabetically before signing to prevent ordering ambiguity.
        val stringToSign = listOf(
            "appId=${request.appId}",
            "productCode=${request.productCode}",
            "quantity=${request.quantity}",
            "timestamp=${request.timestamp}"
        ).sorted().joinToString("&")

        if (!platformAuthLogic.verifySignature(client.appSecret, stringToSign, request.signature)) {
            throw signatureInvalid()
        }

        // Create a charge record via ChargeLogic
        val charge = chargeLogic ?: throw chargeServiceUnavailable()
        val hexChars = "0123456789abcdef"
        val randomSuffix = Random.Default.nextBytes(4).joinToString("") { byte ->
            val b = byte.toInt() and 0xFF
            "${hexChars[b ushr 4]}${hexChars[b and 0x0F]}"
        }
        val orderId = "ORD_${client.appId}_${Clock.System.now().toEpochMilliseconds()}_$randomSuffix"
        charge.createChargeRecord(ChargeRecord(
            clientId = client.id,
            apiId = 0,
            orderId = orderId,
            apiCode = request.productCode,
            amount = 0,
            status = 0
        ))

        return OrderCreateResponse(orderId = orderId)
    }

    // ─── errors ────────────────────────────────────────────────────────────

    private fun checkTimestamp(timestamp: String) {
        val ts = timestamp.toLongOrNull()
            ?: throw HttpException(
                NetonErrorCode.INVALID_FORMAT,
                PlatformErrorCodes.TIMESTAMP_INVALID,
                listOf(ValidationError("timestamp", PlatformErrorCodes.TIMESTAMP_INVALID, PlatformErrorCodes.TIMESTAMP_INVALID))
            )
        val nowSeconds = Clock.System.now().toEpochMilliseconds() / 1000
        if (kotlin.math.abs(nowSeconds - ts) > 300) {
            throw HttpException(
                NetonErrorCode.INVALID_PARAMS,
                PlatformErrorCodes.TIMESTAMP_EXPIRED,
                listOf(ValidationError("timestamp", PlatformErrorCodes.TIMESTAMP_EXPIRED, PlatformErrorCodes.TIMESTAMP_EXPIRED))
            )
        }
    }

    private fun appIdInvalid() = HttpException(
        NetonErrorCode.INVALID_CREDENTIALS,
        PlatformErrorCodes.APP_ID_INVALID,
        listOf(ValidationError("appId", PlatformErrorCodes.APP_ID_INVALID, PlatformErrorCodes.APP_ID_INVALID))
    )

    private fun signatureInvalid() = HttpException(
        NetonErrorCode.INVALID_CREDENTIALS,
        PlatformErrorCodes.SIGNATURE_INVALID,
        listOf(ValidationError("signature", PlatformErrorCodes.SIGNATURE_INVALID, PlatformErrorCodes.SIGNATURE_INVALID))
    )

    private fun chargeServiceUnavailable() =
        HttpException(NetonErrorCode.SERVICE_UNAVAILABLE, PlatformErrorCodes.CHARGE_SERVICE_UNAVAILABLE)
}
