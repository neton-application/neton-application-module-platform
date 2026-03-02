package controller.admin.client.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientVO(
    val id: Long = 0,
    val name: String? = null,
    val appId: String? = null,
    val appSecret: String? = null,
    val status: Int? = null,
    val remark: String? = null,
    val contactName: String? = null,
    val contactMobile: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
