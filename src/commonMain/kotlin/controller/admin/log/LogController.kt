package controller.admin.log

import controller.admin.log.dto.CreatePlatformLogRequest
import controller.admin.log.dto.PlatformLogVO
import logic.ChargeLogic
import model.PlatformLog
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Permission
import neton.core.annotations.Post
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/log")
class LogController(private val chargeLogic: ChargeLogic) {

    @Post("/create")
    @Permission("platform:log:create")
    suspend fun create(@Body request: CreatePlatformLogRequest): Long {
        return chargeLogic.createLog(
            PlatformLog(
                clientId = request.clientId,
                apiId = request.apiId,
                requestUrl = request.requestUrl,
                requestParams = request.requestParams,
                responseBody = request.responseBody,
                userIp = request.userIp,
                duration = request.duration,
                resultCode = request.resultCode
            )
        )
    }

    @Delete("/delete/{id}")
    @Permission("platform:log:delete")
    suspend fun delete(@PathVariable id: Long) {
        chargeLogic.deletePlatformLog(id)
    }

    @Delete("/delete-list")
    @Permission("platform:log:delete")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        chargeLogic.deletePlatformLogByIds(idList)
    }

    @Get("/get/{id}")
    @Permission("platform:log:query")
    suspend fun get(@PathVariable id: Long): PlatformLog? {
        return chargeLogic.getPlatformLog(id)
    }

    @Get("/page")
    @Permission("platform:log:page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query clientId: Long? = null,
        @Query apiId: Long? = null,
        @Query resultCode: Int? = null
    ) = chargeLogic.pageLogs(pageNo, pageSize, clientId, apiId, resultCode)
}
