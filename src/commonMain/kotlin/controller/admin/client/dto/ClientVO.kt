package controller.admin.client.dto

import kotlinx.serialization.Serializable
import neton.validation.annotations.Max
import neton.validation.annotations.Min
import neton.validation.annotations.NotBlank
import neton.validation.annotations.Pattern
import neton.validation.annotations.Size

@Serializable
data class ClientVO(
    val id: Long = 0,
    val name: String? = null,
    val appId: String? = null,
    val appSecret: String? = null,
    val status: Int? = null,
    val remark: String? = null,
    val contactName: String? = null,
    val contactMobile: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class CreateClientRequest(
    @property:NotBlank
    @property:Size(min = 2, max = 64)
    val name: String,

    @property:NotBlank
    @property:Size(min = 8, max = 64)
    val appId: String,

    @property:NotBlank
    @property:Size(min = 16, max = 128)
    val appSecret: String,

    @property:Min(0)
    @property:Max(1)
    val status: Int = 1,

    @property:Size(min = 0, max = 255)
    val remark: String? = null,

    @property:Size(min = 0, max = 64)
    val contactName: String? = null,

    @property:Pattern(regex = "^1\\d{10}$", message = "contactMobile format is invalid")
    val contactMobile: String? = null
)

@Serializable
data class UpdateClientRequest(
    @property:Min(1)
    val id: Long,

    @property:NotBlank
    @property:Size(min = 2, max = 64)
    val name: String,

    @property:NotBlank
    @property:Size(min = 8, max = 64)
    val appId: String,

    @property:NotBlank
    @property:Size(min = 16, max = 128)
    val appSecret: String,

    @property:Min(0)
    @property:Max(1)
    val status: Int = 1,

    @property:Size(min = 0, max = 255)
    val remark: String? = null,

    @property:Size(min = 0, max = 64)
    val contactName: String? = null,

    @property:Pattern(regex = "^1\\d{10}$", message = "contactMobile format is invalid")
    val contactMobile: String? = null
)
