package controller.admin.stat.dto

import kotlinx.serialization.Serializable
import neton.validation.annotations.Min
import neton.validation.annotations.Pattern
import neton.validation.annotations.Size

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

@Serializable
data class CreatePlatformStatRequest(
    @property:Min(1)
    val clientId: Long,

    @property:Min(1)
    val apiId: Long,

    @property:Min(0)
    val callCount: Long = 0,

    @property:Min(0)
    val totalCharge: Long = 0,

    @property:Pattern("^\\d{4}-\\d{2}-\\d{2}$")
    @property:Size(min = 10, max = 10)
    val statDate: String
)

@Serializable
data class UpdatePlatformStatRequest(
    @property:Min(1)
    val id: Long,

    @property:Min(1)
    val clientId: Long,

    @property:Min(1)
    val apiId: Long,

    @property:Min(0)
    val callCount: Long = 0,

    @property:Min(0)
    val totalCharge: Long = 0,

    @property:Pattern("^\\d{4}-\\d{2}-\\d{2}$")
    @property:Size(min = 10, max = 10)
    val statDate: String
)
