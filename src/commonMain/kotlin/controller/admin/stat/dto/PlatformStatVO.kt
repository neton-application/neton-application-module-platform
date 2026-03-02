package controller.admin.stat.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlatformStatVO(
    val id: Long = 0,
    val clientId: Long? = null,
    val apiId: Long? = null,
    val callCount: Long = 0,
    val totalCharge: Long = 0,
    val statDate: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
