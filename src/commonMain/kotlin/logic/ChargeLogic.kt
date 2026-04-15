package logic

import dto.PageResponse
import model.ChargeRecord
import model.PlatformLog
import model.PlatformStat
import table.ChargeRecordTable
import table.PlatformLogTable
import table.PlatformStatTable
import neton.database.dsl.*

import neton.logging.Logger

class ChargeLogic(
    private val log: Logger
) {

    // ===== ChargeRecord =====

    suspend fun createChargeRecord(record: ChargeRecord): Long {
        val inserted = ChargeRecordTable.insert(record)
        log.info("Created charge record with id: ${inserted.id}, clientId: ${record.clientId}, apiId: ${record.apiId}, price: ${record.price}")
        return inserted.id
    }

    suspend fun getChargeRecord(id: Long): ChargeRecord? {
        return ChargeRecordTable.get(id)
    }

    suspend fun getChargeRecordByOrderId(orderId: String): ChargeRecord? {
        return ChargeRecordTable.oneWhere {
            ChargeRecord::orderId eq orderId
        }
    }

    suspend fun deleteChargeRecord(id: Long) {
        ChargeRecordTable.destroy(id)
        log.info("Deleted charge record with id: $id")
    }

    suspend fun deleteChargeRecordByIds(ids: List<Long>) {
        ids.forEach { deleteChargeRecord(it) }
    }

    suspend fun pageCharges(
        page: Int,
        size: Int,
        clientId: Long? = null,
        apiId: Long? = null
    ): PageResponse<ChargeRecord> {
        val result = ChargeRecordTable.query {
            where {
                and(
                    whenPresent(clientId) { ChargeRecord::clientId eq it },
                    whenPresent(apiId) { ChargeRecord::apiId eq it }
                )
            }
            orderBy(ChargeRecord::id.desc())
        }.page(page, size)
        return PageResponse(result.items, result.total, page, size,
            if (size > 0) ((result.total + size - 1) / size).toInt() else 0)
    }

    // ===== PlatformLog =====

    suspend fun createLog(platformLog: PlatformLog): Long {
        return PlatformLogTable.insert(platformLog).id
    }

    suspend fun getPlatformLog(id: Long): PlatformLog? {
        return PlatformLogTable.get(id)
    }

    suspend fun deletePlatformLog(id: Long) {
        PlatformLogTable.destroy(id)
        log.info("Deleted platform log with id: $id")
    }

    suspend fun deletePlatformLogByIds(ids: List<Long>) {
        ids.forEach { deletePlatformLog(it) }
    }

    suspend fun pageLogs(
        page: Int,
        size: Int,
        clientId: Long? = null,
        apiId: Long? = null,
        resultCode: Int? = null
    ): PageResponse<PlatformLog> {
        val result = PlatformLogTable.query {
            where {
                and(
                    whenPresent(clientId) { PlatformLog::clientId eq it },
                    whenPresent(apiId) { PlatformLog::apiId eq it },
                    whenPresent(resultCode) { PlatformLog::resultCode eq it }
                )
            }
            orderBy(PlatformLog::id.desc())
        }.page(page, size)
        return PageResponse(result.items, result.total, page, size,
            if (size > 0) ((result.total + size - 1) / size).toInt() else 0)
    }

    // ===== PlatformStat =====

    suspend fun createStat(stat: PlatformStat): Long {
        return PlatformStatTable.insert(stat).id
    }

    suspend fun updateStat(stat: PlatformStat) {
        PlatformStatTable.update(stat)
    }

    suspend fun getStat(id: Long): PlatformStat? {
        return PlatformStatTable.get(id)
    }

    suspend fun deleteStat(id: Long) {
        PlatformStatTable.destroy(id)
        log.info("Deleted platform stat with id: $id")
    }

    suspend fun deleteStatByIds(ids: List<Long>) {
        ids.forEach { deleteStat(it) }
    }

    suspend fun pageStats(
        page: Int,
        size: Int,
        clientId: Long? = null,
        apiId: Long? = null,
        statDate: String? = null
    ): PageResponse<PlatformStat> {
        val result = PlatformStatTable.query {
            where {
                and(
                    whenPresent(clientId) { PlatformStat::clientId eq it },
                    whenPresent(apiId) { PlatformStat::apiId eq it },
                    whenNotBlank(statDate) { PlatformStat::statDate eq it }
                )
            }
            orderBy(PlatformStat::id.desc())
        }.page(page, size)
        return PageResponse(result.items, result.total, page, size,
            if (size > 0) ((result.total + size - 1) / size).toInt() else 0)
    }
}
