package logic

import dto.PageResponse
import model.Client
import table.ClientTable
import neton.database.dsl.*

import neton.logging.Logger
import kotlin.random.Random

class ClientLogic(
    private val log: Logger
) {

    suspend fun create(client: Client): Long {
        val inserted = ClientTable.insert(client)
        log.info("Created client with id: ${inserted.id}, name: ${client.name}")
        return inserted.id
    }

    suspend fun update(client: Client) {
        ClientTable.update(client)
        log.info("Updated client with id: ${client.id}")
    }

    suspend fun delete(id: Long) {
        ClientTable.destroy(id)
        log.info("Deleted client with id: $id")
    }

    suspend fun deleteByIds(ids: List<Long>) {
        ids.forEach { delete(it) }
    }

    suspend fun get(id: Long): Client? {
        return ClientTable.get(id)
    }

    suspend fun getByUserId(userId: Long): Client? {
        return ClientTable.oneWhere {
            and(
                Client::userId eq userId,
                Client::status eq 1
            )
        }
    }

    suspend fun page(
        page: Int,
        size: Int,
        name: String? = null,
        appId: String? = null,
        status: Int? = null
    ): PageResponse<Client> {
        val result = ClientTable.query {
            where {
                and(
                    whenNotBlank(name) { Client::name like "%$it%" },
                    whenNotBlank(appId) { Client::appId eq it },
                    whenPresent(status) { Client::status eq it }
                )
            }
            orderBy(Client::id.desc())
        }.page(page, size)
        return PageResponse(result.items, result.total, page, size,
            if (size > 0) ((result.total + size - 1) / size).toInt() else 0)
    }

    fun generateAppId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val prefix = "neton_"
        val randomPart = (1..24).map { chars[Random.nextInt(chars.length)] }.joinToString("")
        return prefix + randomPart
    }

    fun generateAppSecret(): String {
        // Use OS-backed CSPRNG via Random.Default.nextBytes (Kotlin/Native uses /dev/urandom)
        val hexChars = "0123456789abcdef"
        return Random.Default.nextBytes(36).joinToString("") { byte ->
            val b = byte.toInt() and 0xFF
            "${hexChars[b ushr 4]}${hexChars[b and 0x0F]}"
        }
    }
}
