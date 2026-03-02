package controller.admin.client

import controller.admin.client.dto.ClientVO
import logic.ClientLogic
import model.Client
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Post
import neton.core.annotations.Put
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/client")
class ClientController(private val clientLogic: ClientLogic) {

    @Post("/create")
    suspend fun create(@Body client: Client): Long {
        return clientLogic.create(client)
    }

    @Put("/update")
    suspend fun update(@Body client: Client) {
        clientLogic.update(client)
    }

    @Delete("/delete/{id}")
    suspend fun delete(@PathVariable id: Long) {
        clientLogic.delete(id)
    }

    @Delete("/delete-list")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        clientLogic.deleteByIds(idList)
    }

    @Get("/get/{id}")
    suspend fun get(@PathVariable id: Long): Client? {
        return clientLogic.get(id)
    }

    @Get("/page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query name: String? = null,
        @Query appId: String? = null,
        @Query status: Int? = null
    ) = clientLogic.page(pageNo, pageSize, name, appId, status)

    @Get("/generateAppId")
    suspend fun generateAppId(): Map<String, String> {
        return mapOf("appId" to clientLogic.generateAppId())
    }

    @Get("/generateAppSecret")
    suspend fun generateAppSecret(): Map<String, String> {
        return mapOf("appSecret" to clientLogic.generateAppSecret())
    }
}
