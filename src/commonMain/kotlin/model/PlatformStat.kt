package model

import kotlinx.serialization.Serializable
import neton.database.annotations.Table
import neton.database.annotations.Id
import neton.database.annotations.CreatedAt
import neton.database.annotations.UpdatedAt

@Serializable
@Table("platform_stats")
data class PlatformStat(
    @Id
    val id: Long = 0,
    val clientId: Long,
    val apiId: Long,
    val callCount: Long = 0,
    val totalCharge: Long = 0,
    val statDate: String,
    @CreatedAt
    val createdAt: String? = null,
    @UpdatedAt
    val updatedAt: String? = null
)
