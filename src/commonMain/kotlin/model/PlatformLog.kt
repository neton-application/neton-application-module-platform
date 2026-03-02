package model

import kotlinx.serialization.Serializable
import neton.database.annotations.Table
import neton.database.annotations.Id
import neton.database.annotations.CreatedAt

@Serializable
@Table("platform_logs")
data class PlatformLog(
    @Id
    val id: Long = 0,
    val clientId: Long,
    val apiId: Long,
    val requestUrl: String? = null,
    val requestParams: String? = null,
    val responseBody: String? = null,
    val userIp: String? = null,
    val duration: Long = 0,
    val resultCode: Int = 0,
    @CreatedAt
    val createdAt: String? = null
)
