package controller.admin.charge

import controller.admin.charge.dto.CreateChargeRecordRequest
import controller.admin.charge.dto.ChargeRecordVO
import logic.ChargeLogic
import model.ChargeRecord
import neton.core.annotations.Controller
import neton.core.annotations.Get
import neton.core.annotations.Permission
import neton.core.annotations.Post
import neton.core.annotations.Delete
import neton.core.annotations.Body
import neton.core.annotations.PathVariable
import neton.core.annotations.Query

@Controller("/platform/charge-record")
class ChargeRecordController(private val chargeLogic: ChargeLogic) {

    @Post("/create")
    @Permission("platform:charge:create")
    suspend fun create(@Body request: CreateChargeRecordRequest): Long {
        return chargeLogic.createChargeRecord(
            ChargeRecord(
                clientId = request.clientId,
                apiId = request.apiId,
                orderId = request.orderId,
                apiCode = request.apiCode,
                price = request.price,
                amount = request.amount,
                status = request.status
            )
        )
    }

    @Delete("/delete/{id}")
    @Permission("platform:charge:delete")
    suspend fun delete(@PathVariable id: Long) {
        chargeLogic.deleteChargeRecord(id)
    }

    @Delete("/delete-list")
    @Permission("platform:charge:delete")
    suspend fun deleteList(@Query ids: String) {
        val idList = ids.split(",").mapNotNull { it.trim().toLongOrNull() }
        chargeLogic.deleteChargeRecordByIds(idList)
    }

    @Get("/get/{id}")
    @Permission("platform:charge:query")
    suspend fun get(@PathVariable id: Long): ChargeRecord? {
        return chargeLogic.getChargeRecord(id)
    }

    @Get("/page")
    @Permission("platform:charge:page")
    suspend fun page(
        @Query pageNo: Int = 1,
        @Query pageSize: Int = 20,
        @Query clientId: Long? = null,
        @Query apiId: Long? = null
    ) = chargeLogic.pageCharges(pageNo, pageSize, clientId, apiId)
}
