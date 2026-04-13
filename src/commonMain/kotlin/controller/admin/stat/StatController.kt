package controller.admin.stat

import controller.admin.stat.dto.CreatePlatformStatRequest
import controller.admin.stat.dto.PlatformStatVO
import controller.admin.stat.dto.UpdatePlatformStatRequest
import logic.ChargeLogic
import model.PlatformStat
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Post
import neton.core.annotations.Put
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/stat")
class StatController(private val chargeLogic: ChargeLogic) {

    @Post("/create")
    suspend fun create(@Body request: CreatePlatformStatRequest): Long {
        return chargeLogic.createStat(
            PlatformStat(
                clientId = request.clientId,
                apiId = request.apiId,
                callCount = request.callCount,
                totalCharge = request.totalCharge,
                statDate = request.statDate
            )
        )
    }

    @Put("/update")
    suspend fun update(@Body request: UpdatePlatformStatRequest) {
        chargeLogic.updateStat(
            PlatformStat(
                id = request.id,
                clientId = request.clientId,
                apiId = request.apiId,
                callCount = request.callCount,
                totalCharge = request.totalCharge,
                statDate = request.statDate
            )
        )
    }

    @Delete("/delete/{id}")
    suspend fun delete(@PathVariable id: Long) {
        chargeLogic.deleteStat(id)
    }

    @Delete("/delete-list")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        chargeLogic.deleteStatByIds(idList)
    }

    @Get("/get/{id}")
    suspend fun get(@PathVariable id: Long): PlatformStat? {
        return chargeLogic.getStat(id)
    }

    @Get("/page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query clientId: Long? = null,
        @Query apiId: Long? = null,
        @Query statDate: String? = null
    ) = chargeLogic.pageStats(pageNo, pageSize, clientId, apiId, statDate)
}
