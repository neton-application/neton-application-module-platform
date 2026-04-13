package controller.admin.log.dto

import kotlinx.serialization.Serializable
import neton.validation.annotations.Min
import neton.validation.annotations.Size

@Serializable
data class PlatformLogVO(
    val id: Long = 0,
    val clientId: Long? = null,
    val apiId: Long? = null,
    val requestUrl: String? = null,
    val requestParams: String? = null,
    val responseBody: String? = null,
    val userIp: String? = null,
    val duration: Long = 0,
    val resultCode: Int = 0,
    val createdAt: String? = null
)

@Serializable
data class CreatePlatformLogRequest(
    @property:Min(1)
    val clientId: Long,

    @property:Min(1)
    val apiId: Long,

    @property:Size(min = 0, max = 500)
    val requestUrl: String? = null,

    @property:Size(min = 0, max = 4000)
    val requestParams: String? = null,

    @property:Size(min = 0, max = 4000)
    val responseBody: String? = null,

    @property:Size(min = 0, max = 64)
    val userIp: String? = null,

    @property:Min(0)
    val duration: Long = 0,

    val resultCode: Int = 0
)
