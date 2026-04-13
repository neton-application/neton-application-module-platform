package model

import kotlinx.serialization.Serializable
import neton.database.annotations.Table
import neton.database.annotations.Id
import neton.database.annotations.CreatedAt

@Serializable
@Table("platform_charge_records")
data class ChargeRecord(
    @Id
    val id: Long = 0,
    val clientId: Long,
    val apiId: Long,
    val orderId: String? = null,
    val apiCode: String? = null,
    val price: Long = 0,
    val amount: Long = 0,
    val status: Int = 1,
    @CreatedAt
    val createdAt: String? = null
)
