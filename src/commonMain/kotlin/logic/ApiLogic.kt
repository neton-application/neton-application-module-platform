package logic

import dto.PageResponse
import model.Api
import model.ClientApi
import table.ApiTable
import table.ClientApiTable
import neton.database.dsl.*

import neton.logging.Logger

class ApiLogic(
    private val log: Logger
) {

    // ===== Api CRUD =====

    suspend fun create(api: Api): Long {
        val inserted = ApiTable.insert(api)
        log.info("Created api with id: ${inserted.id}, name: ${api.name}")
        return inserted.id
    }

    suspend fun update(api: Api) {
        ApiTable.update(api)
        log.info("Updated api with id: ${api.id}")
    }

    suspend fun delete(id: Long) {
        ApiTable.destroy(id)
        log.info("Deleted api with id: $id")
    }

    suspend fun deleteByIds(ids: List<Long>) {
        ids.forEach { delete(it) }
    }

    suspend fun get(id: Long): Api? {
        return ApiTable.get(id)
    }

    suspend fun list(): List<Api> {
        return ApiTable.query {
            orderBy(Api::id.desc())
        }.list()
    }

    suspend fun page(
        page: Int,
        size: Int,
        name: String? = null,
        code: String? = null,
        status: Int? = null
    ): PageResponse<Api> {
        val result = ApiTable.query {
            where {
                and(
                    whenNotBlank(name) { Api::name like "%$it%" },
                    whenNotBlank(code) { Api::code like "%$it%" },
                    whenPresent(status) { Api::status eq it }
                )
            }
            orderBy(Api::id.desc())
        }.page(page, size)
        return PageResponse(result.items, result.total, page, size,
            if (size > 0) ((result.total + size - 1) / size).toInt() else 0)
    }

    // ===== ClientApi (Authorization) CRUD =====

    suspend fun createClientApi(clientApi: ClientApi): Long {
        val inserted = ClientApiTable.insert(clientApi)
        log.info("Created client-api authorization with id: ${inserted.id}, clientId: ${clientApi.clientId}, apiId: ${clientApi.apiId}")
        return inserted.id
    }

    suspend fun updateClientApi(clientApi: ClientApi) {
        ClientApiTable.update(clientApi)
        log.info("Updated client-api authorization with id: ${clientApi.id}")
    }

    suspend fun deleteClientApi(id: Long) {
        ClientApiTable.destroy(id)
        log.info("Deleted client-api authorization with id: $id")
    }

    suspend fun deleteClientApiByIds(ids: List<Long>) {
        ids.forEach { deleteClientApi(it) }
    }

    suspend fun getClientApi(id: Long): ClientApi? {
        return ClientApiTable.get(id)
    }

    suspend fun getByClientIdAndApiId(clientId: Long, apiId: Long): ClientApi? {
        return ClientApiTable.oneWhere {
            and(
                ClientApi::clientId eq clientId,
                ClientApi::apiId eq apiId
            )
        }
    }

    suspend fun pageClientApi(
        page: Int,
        size: Int,
        clientId: Long? = null,
        apiId: Long? = null,
        status: Int? = null
    ): PageResponse<ClientApi> {
        val result = ClientApiTable.query {
            where {
                and(
                    whenPresent(clientId) { ClientApi::clientId eq it },
                    whenPresent(apiId) { ClientApi::apiId eq it },
                    whenPresent(status) { ClientApi::status eq it }
                )
            }
            orderBy(ClientApi::id.desc())
        }.page(page, size)
        return PageResponse(result.items, result.total, page, size,
            if (size > 0) ((result.total + size - 1) / size).toInt() else 0)
    }
}
