package controller.admin.client

import controller.admin.client.dto.CreateClientApiRequest
import controller.admin.client.dto.ClientApiVO
import controller.admin.client.dto.UpdateClientApiRequest
import logic.ApiLogic
import model.ClientApi
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Post
import neton.core.annotations.Put
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/client-api")
class ClientApiController(private val apiLogic: ApiLogic) {

    @Post("/create")
    suspend fun create(@Body request: CreateClientApiRequest): Long {
        return apiLogic.createClientApi(
            ClientApi(
                clientId = request.clientId,
                apiId = request.apiId,
                customPrice = request.customPrice,
                status = request.status
            )
        )
    }

    @Put("/update")
    suspend fun update(@Body request: UpdateClientApiRequest) {
        apiLogic.updateClientApi(
            ClientApi(
                id = request.id,
                clientId = request.clientId,
                apiId = request.apiId,
                customPrice = request.customPrice,
                status = request.status
            )
        )
    }

    @Delete("/delete/{id}")
    suspend fun delete(@PathVariable id: Long) {
        apiLogic.deleteClientApi(id)
    }

    @Delete("/delete-list")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        apiLogic.deleteClientApiByIds(idList)
    }

    @Get("/get/{id}")
    suspend fun get(@PathVariable id: Long): ClientApi? {
        return apiLogic.getClientApi(id)
    }

    @Get("/byClientIdAndApiId")
    suspend fun byClientIdAndApiId(@Query clientId: Long, @Query apiId: Long): ClientApi? {
        return apiLogic.getByClientIdAndApiId(clientId, apiId)
    }

    @Post("/createAssociation")
    suspend fun createAssociation(@Body request: CreateClientApiRequest): Long {
        return apiLogic.createClientApi(
            ClientApi(
                clientId = request.clientId,
                apiId = request.apiId,
                customPrice = request.customPrice,
                status = request.status
            )
        )
    }

    @Get("/page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query clientId: Long? = null,
        @Query apiId: Long? = null,
        @Query status: Int? = null
    ) = apiLogic.pageClientApi(pageNo, pageSize, clientId, apiId, status)
}
