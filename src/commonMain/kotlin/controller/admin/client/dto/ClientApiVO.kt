package controller.admin.client.dto

import kotlinx.serialization.Serializable
import neton.validation.annotations.Max
import neton.validation.annotations.Min

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

@Serializable
data class CreateClientApiRequest(
    @property:Min(1)
    val clientId: Long,

    @property:Min(1)
    val apiId: Long,

    @property:Min(0)
    val customPrice: Long? = null,

    @property:Min(0)
    @property:Max(1)
    val status: Int = 1
)

@Serializable
data class UpdateClientApiRequest(
    @property:Min(1)
    val id: Long,

    @property:Min(1)
    val clientId: Long,

    @property:Min(1)
    val apiId: Long,

    @property:Min(0)
    val customPrice: Long? = null,

    @property:Min(0)
    @property:Max(1)
    val status: Int = 1
)
