package controller.admin.charge.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChargeRecordVO(
    val id: Long = 0,
    val clientId: Long? = null,
    val apiId: Long? = null,
    val price: Long? = null,
    val createdAt: String? = null
)
