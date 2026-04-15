package controller.admin.client

import controller.admin.client.dto.CreateClientRequest
import controller.admin.client.dto.ClientVO
import controller.admin.client.dto.UpdateClientRequest
import dto.PageResponse
import logic.ClientLogic
import model.Client
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Permission
import neton.core.annotations.Post
import neton.core.annotations.Put
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/client")
class ClientController(private val clientLogic: ClientLogic) {

    @Post("/create")
    @Permission("platform:client:create")
    suspend fun create(@Body request: CreateClientRequest): Long {
        return clientLogic.create(
            Client(
                name = request.name,
                appId = request.appId,
                appSecret = request.appSecret,
                status = request.status,
                remark = request.remark,
                contactName = request.contactName,
                contactMobile = request.contactMobile
            )
        )
    }

    @Put("/update")
    @Permission("platform:client:update")
    suspend fun update(@Body request: UpdateClientRequest) {
        clientLogic.update(
            Client(
                id = request.id,
                name = request.name,
                appId = request.appId,
                appSecret = request.appSecret,
                status = request.status,
                remark = request.remark,
                contactName = request.contactName,
                contactMobile = request.contactMobile
            )
        )
    }

    @Delete("/delete/{id}")
    @Permission("platform:client:delete")
    suspend fun delete(@PathVariable id: Long) {
        clientLogic.delete(id)
    }

    @Delete("/delete-list")
    @Permission("platform:client:delete")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        clientLogic.deleteByIds(idList)
    }

    @Get("/get/{id}")
    @Permission("platform:client:query")
    suspend fun get(@PathVariable id: Long): ClientVO? {
        return clientLogic.get(id)?.toMaskedVO()
    }

    @Get("/page")
    @Permission("platform:client:page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query name: String? = null,
        @Query appId: String? = null,
        @Query status: Int? = null
    ): PageResponse<ClientVO> {
        val result = clientLogic.page(pageNo, pageSize, name, appId, status)
        return PageResponse(result.list.map { it.toMaskedVO() }, result.total, result.page, result.size, result.totalPages)
    }

    private fun Client.toMaskedVO(): ClientVO {
        val masked = appSecret?.let {
            if (it.length > 8) it.take(8) + "****" else "****"
        }
        return ClientVO(
            id = id,
            name = name,
            appId = appId,
            appSecret = masked,
            status = status,
            remark = remark,
            contactName = contactName,
            contactMobile = contactMobile,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    @Get("/generateAppId")
    @Permission("platform:client:create")
    suspend fun generateAppId(): Map<String, String> {
        return mapOf("appId" to clientLogic.generateAppId())
    }

    @Get("/generateAppSecret")
    @Permission("platform:client:create")
    suspend fun generateAppSecret(): Map<String, String> {
        return mapOf("appSecret" to clientLogic.generateAppSecret())
    }
}
