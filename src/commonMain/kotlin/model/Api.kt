package model

import kotlinx.serialization.Serializable
import neton.database.annotations.Table
import neton.database.annotations.Id
import neton.database.annotations.CreatedAt
import neton.database.annotations.UpdatedAt

@Serializable
@Table("platform_apis")
data class Api(
    @Id
    val id: Long = 0,
    val name: String,
    val code: String,
    val description: String? = null,
    val price: Long = 0,
    val status: Int = 0,
    @CreatedAt
    val createdAt: String? = null,
    @UpdatedAt
    val updatedAt: String? = null
)
