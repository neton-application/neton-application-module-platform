package model

import kotlinx.serialization.Serializable
import neton.database.annotations.Table
import neton.database.annotations.Id
import neton.database.annotations.CreatedAt
import neton.database.annotations.UpdatedAt

@Serializable
@Table("platform_client_apis")
data class ClientApi(
    @Id
    val id: Long = 0,
    val clientId: Long,
    val apiId: Long,
    val customPrice: Long? = null,
    val status: Int = 1,
    @CreatedAt
    val createdAt: String? = null,
    @UpdatedAt
    val updatedAt: String? = null
)
