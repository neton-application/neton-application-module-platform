package controller.admin.api

import controller.admin.api.dto.ApiVO
import logic.ApiLogic
import model.Api
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Post
import neton.core.annotations.Put
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/api")
class ApiController(private val apiLogic: ApiLogic) {

    @Post("/create")
    suspend fun create(@Body api: Api): Long {
        return apiLogic.create(api)
    }

    @Put("/update")
    suspend fun update(@Body api: Api) {
        apiLogic.update(api)
    }

    @Delete("/delete/{id}")
    suspend fun delete(@PathVariable id: Long) {
        apiLogic.delete(id)
    }

    @Delete("/delete-list")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        apiLogic.deleteByIds(idList)
    }

    @Get("/get/{id}")
    suspend fun get(@PathVariable id: Long): Api? {
        return apiLogic.get(id)
    }

    @Get("/list")
    suspend fun list(): List<Api> {
        return apiLogic.list()
    }

    @Get("/page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query name: String? = null,
        @Query code: String? = null,
        @Query status: Int? = null
    ) = apiLogic.page(pageNo, pageSize, name, code, status)
}
