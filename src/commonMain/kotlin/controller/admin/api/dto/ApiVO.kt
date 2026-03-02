package controller.admin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiVO(
    val id: Long = 0,
    val name: String? = null,
    val code: String? = null,
    val description: String? = null,
    val price: Long? = null,
    val status: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
