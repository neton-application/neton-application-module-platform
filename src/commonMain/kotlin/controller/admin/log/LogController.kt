package controller.admin.log

import controller.admin.log.dto.CreatePlatformLogRequest
import controller.admin.log.dto.PlatformLogVO
import logic.ChargeLogic
import model.PlatformLog
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Post
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/log")
class LogController(private val chargeLogic: ChargeLogic) {

    @Post("/create")
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
    suspend fun delete(@PathVariable id: Long) {
        chargeLogic.deletePlatformLog(id)
    }

    @Delete("/delete-list")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        chargeLogic.deletePlatformLogByIds(idList)
    }

    @Get("/get/{id}")
    suspend fun get(@PathVariable id: Long): PlatformLog? {
        return chargeLogic.getPlatformLog(id)
    }

    @Get("/page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query clientId: Long? = null,
        @Query apiId: Long? = null,
        @Query resultCode: Int? = null
    ) = chargeLogic.pageLogs(pageNo, pageSize, clientId, apiId, resultCode)
}
