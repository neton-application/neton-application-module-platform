package model

import kotlinx.serialization.Serializable
import neton.database.annotations.Table
import neton.database.annotations.Id
import neton.database.annotations.SoftDelete
import neton.database.annotations.CreatedAt
import neton.database.annotations.UpdatedAt

@Serializable
@Table("platform_clients")
data class Client(
    @Id
    val id: Long = 0,
    val name: String,
    val appId: String,
    val appSecret: String,
    val status: Int = 1,
    val remark: String? = null,
    val contactName: String? = null,
    val contactMobile: String? = null,
    @SoftDelete
    val deleted: Int = 0,
    @CreatedAt
    val createdAt: String? = null,
    @UpdatedAt
    val updatedAt: String? = null
)
