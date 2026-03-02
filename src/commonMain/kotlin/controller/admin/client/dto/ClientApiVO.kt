package controller.admin.client.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientApiVO(
    val id: Long = 0,
    val clientId: Long? = null,
    val apiId: Long? = null,
    val customPrice: Long? = null,
    val status: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
