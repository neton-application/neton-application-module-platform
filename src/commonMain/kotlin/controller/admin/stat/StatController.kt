package controller.admin.stat

import controller.admin.stat.dto.PlatformStatVO
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
    suspend fun create(@Body stat: PlatformStat): Long {
        return chargeLogic.createStat(stat)
    }

    @Put("/update")
    suspend fun update(@Body stat: PlatformStat) {
        chargeLogic.updateStat(stat)
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
