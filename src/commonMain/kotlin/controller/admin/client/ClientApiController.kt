package controller.admin.client

import controller.admin.client.dto.ClientApiVO
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
    suspend fun create(@Body clientApi: ClientApi): Long {
        return apiLogic.createClientApi(clientApi)
    }

    @Put("/update")
    suspend fun update(@Body clientApi: ClientApi) {
        apiLogic.updateClientApi(clientApi)
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
    suspend fun createAssociation(@Body clientApi: ClientApi): Long {
        return apiLogic.createClientApi(clientApi)
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
