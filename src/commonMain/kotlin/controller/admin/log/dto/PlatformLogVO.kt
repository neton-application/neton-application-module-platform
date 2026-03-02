package controller.admin.log.dto

import kotlinx.serialization.Serializable

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
