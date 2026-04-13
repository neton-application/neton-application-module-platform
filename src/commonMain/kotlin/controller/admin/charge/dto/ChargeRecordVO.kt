package controller.admin.charge.dto

import kotlinx.serialization.Serializable
import neton.validation.annotations.Min
import neton.validation.annotations.Size

@Serializable
data class ChargeRecordVO(
    val id: Long = 0,
    val clientId: Long? = null,
    val apiId: Long? = null,
    val price: Long? = null,
    val createdAt: String? = null
)

@Serializable
data class CreateChargeRecordRequest(
    @property:Min(1)
    val clientId: Long,

    @property:Min(1)
    val apiId: Long,

    @property:Size(min = 0, max = 100)
    val orderId: String? = null,

    @property:Size(min = 0, max = 100)
    val apiCode: String? = null,

    @property:Min(0)
    val price: Long = 0,

    @property:Min(0)
    val amount: Long = 0,

    @property:Min(0)
    val status: Int = 1
)
