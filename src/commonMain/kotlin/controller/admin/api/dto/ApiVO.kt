package controller.admin.api.dto

import kotlinx.serialization.Serializable
import neton.validation.annotations.Max
import neton.validation.annotations.Min
import neton.validation.annotations.NotBlank
import neton.validation.annotations.Size

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

@Serializable
data class CreateApiRequest(
    @property:NotBlank
    @property:Size(min = 1, max = 64)
    val name: String,

    @property:NotBlank
    @property:Size(min = 1, max = 64)
    val code: String,

    @property:Size(min = 0, max = 255)
    val description: String? = null,

    @property:Min(0)
    val price: Long = 0,

    @property:Min(0)
    @property:Max(1)
    val status: Int = 1
)

@Serializable
data class UpdateApiRequest(
    @property:Min(1)
    val id: Long,

    @property:NotBlank
    @property:Size(min = 1, max = 64)
    val name: String,

    @property:NotBlank
    @property:Size(min = 1, max = 64)
    val code: String,

    @property:Size(min = 0, max = 255)
    val description: String? = null,

    @property:Min(0)
    val price: Long = 0,

    @property:Min(0)
    @property:Max(1)
    val status: Int = 1
)
