package controller.app.account

import dto.PageResponse
import kotlinx.serialization.Serializable
import logic.ApiLogic
import logic.ChargeLogic
import logic.ClientLogic
import neton.core.annotations.*
import neton.core.http.NotFoundException
import neton.core.interfaces.Identity

// ────────────────────────────────────────────────────────────────────────────
// Response DTOs
// ────────────────────────────────────────────────────────────────────────────

@Serializable
data class AccountCredentialVO(
    val clientId: String,
    val clientSecret: String,
    val clientIdMasked: String,
    val clientSecretMasked: String
)

@Serializable
data class AccountSummaryVO(
    val balance: Long,
    val todayCalls: Long,
    val todayChargeAmount: Long,
    val lastCall: LastCallVO? = null
)

@Serializable
data class LastCallVO(
    val apiCode: String,
    val apiName: String,
    val requestTime: String?,
    val resultCode: Int          // 0 = success
)

@Serializable
data class AccountApiVO(
    val apiCode: String,
    val apiName: String,
    val description: String?,
    val price: Long,
    val customPrice: Long?,
    val status: Int
)

@Serializable
data class AccountTrendVO(
    val statDate: String,
    val callCount: Long,
    val totalCharge: Long
)

@Serializable
data class ChargeRecordVO(
    val id: Long,
    val apiCode: String?,
    val apiName: String?,
    val price: Long,        // 单价（分）
    val amount: Long,       // 实际扣除（分）
    val status: Int,        // 1=成功
    val createdAt: String?
)

@Serializable
data class PlatformLogVO(
    val id: Long,
    val apiCode: String?,
    val apiName: String?,
    val requestUrl: String?,
    val requestParams: String?,
    val responseBody: String?,
    val userIp: String?,
    val duration: Long,     // 耗时 ms
    val resultCode: Int,    // 0=成功
    val createdAt: String?
)

// ────────────────────────────────────────────────────────────────────────────
// Controller
// ────────────────────────────────────────────────────────────────────────────

@Controller("/member/account")
class AccountController(
    private val clientLogic: ClientLogic,
    private val chargeLogic: ChargeLogic,
    private val apiLogic: ApiLogic
) {

    /** 获取当前用户的 AppId / AppSecret（脱敏 + 完整） */
    @Get("/credentials")
    suspend fun credentials(identity: Identity): AccountCredentialVO {
        val client = clientLogic.getByUserId(identity.id.toLong())
            ?: throw NotFoundException("No API client bound to current account")
        return AccountCredentialVO(
            clientId = client.appId,
            clientSecret = client.appSecret,
            clientIdMasked = maskMiddle(client.appId),
            clientSecretMasked = maskMiddle(client.appSecret)
        )
    }

    /** 获取当前用户可用的 API 列表（含自定义价格） */
    @Get("/apis")
    suspend fun apis(identity: Identity): List<AccountApiVO> {
        val client = clientLogic.getByUserId(identity.id.toLong())
            ?: throw NotFoundException("No API client bound to current account")

        val clientApis = apiLogic.pageClientApi(1, 200, clientId = client.id, status = 1).list
        val apiIds = clientApis.map { it.apiId }.toSet()
        val apiMap = apiLogic.list().filter { it.id in apiIds && it.status == 1 }
            .associateBy { it.id }

        return clientApis.mapNotNull { clientApi ->
            val api = apiMap[clientApi.apiId] ?: return@mapNotNull null
            AccountApiVO(
                apiCode = api.code,
                apiName = api.name,
                description = api.description,
                price = api.price,
                customPrice = clientApi.customPrice,
                status = clientApi.status
            )
        }
    }

    /** 账户概览：余额、今日调用量、今日扣费、最近一次调用 */
    @Get("/summary")
    suspend fun summary(identity: Identity): AccountSummaryVO {
        val client = clientLogic.getByUserId(identity.id.toLong())
            ?: return AccountSummaryVO(balance = 0, todayCalls = 0, todayChargeAmount = 0)

        val todayStats = chargeLogic.pageStats(1, 1, clientId = client.id).list.firstOrNull()
        val lastLog = chargeLogic.pageLogs(1, 1, clientId = client.id).list.firstOrNull()
        val lastApi = lastLog?.let { apiLogic.get(it.apiId) }

        return AccountSummaryVO(
            balance = 0, // 余额来自 payment 模块，由调用方合并
            todayCalls = todayStats?.callCount ?: 0,
            todayChargeAmount = todayStats?.totalCharge ?: 0,
            lastCall = lastLog?.let { log ->
                LastCallVO(
                    apiCode = lastApi?.code ?: "",
                    apiName = lastApi?.name ?: "",
                    requestTime = log.createdAt,
                    resultCode = log.resultCode
                )
            }
        )
    }

    /** 消费记录分页 */
    @Get("/charges")
    suspend fun charges(
        identity: Identity,
        pageNo: Int = 1,
        pageSize: Int = 20,
        apiCode: String? = null
    ): PageResponse<ChargeRecordVO> {
        val client = clientLogic.getByUserId(identity.id.toLong())
            ?: return PageResponse(emptyList(), 0, pageNo, pageSize, 0)

        val apiList = apiLogic.list()
        val apiByCode = apiList.associateBy { it.code }
        val apiById = apiList.associateBy { it.id }
        val apiId = apiCode?.let { apiByCode[it]?.id }

        val page = chargeLogic.pageCharges(pageNo, pageSize, clientId = client.id, apiId = apiId)
        return PageResponse(
            list = page.list.map { r ->
                val api = apiById[r.apiId]
                ChargeRecordVO(
                    id = r.id,
                    apiCode = r.apiCode ?: api?.code,
                    apiName = api?.name,
                    price = r.price,
                    amount = r.amount,
                    status = r.status,
                    createdAt = r.createdAt
                )
            },
            total = page.total,
            page = page.page,
            size = page.size,
            totalPages = page.totalPages
        )
    }

    /** 请求日志分页；status="SUCCESS" | "FAIL" */
    @Get("/logs")
    suspend fun logs(
        identity: Identity,
        pageNo: Int = 1,
        pageSize: Int = 20,
        apiCode: String? = null,
        status: String? = null
    ): PageResponse<PlatformLogVO> {
        val client = clientLogic.getByUserId(identity.id.toLong())
            ?: return PageResponse(emptyList(), 0, pageNo, pageSize, 0)

        val apiList = apiLogic.list()
        val apiByCode = apiList.associateBy { it.code }
        val apiById = apiList.associateBy { it.id }
        val apiId = apiCode?.let { apiByCode[it]?.id }

        // SUCCESS → resultCode=0，FAIL → 任意非0（用 -1 作为标记，让 Logic 处理 != 0）
        val resultCode: Int? = when (status) {
            "SUCCESS" -> 0
            "FAIL" -> -1   // Logic 层解释为 resultCode != 0
            else -> null
        }

        val page = chargeLogic.pageLogs(pageNo, pageSize, clientId = client.id, apiId = apiId, resultCode = resultCode)
        return PageResponse(
            list = page.list.map { log ->
                val api = apiById[log.apiId]
                PlatformLogVO(
                    id = log.id,
                    apiCode = api?.code,
                    apiName = api?.name,
                    requestUrl = log.requestUrl,
                    requestParams = log.requestParams,
                    responseBody = log.responseBody,
                    userIp = log.userIp,
                    duration = log.duration,
                    resultCode = log.resultCode,
                    createdAt = log.createdAt
                )
            },
            total = page.total,
            page = page.page,
            size = page.size,
            totalPages = page.totalPages
        )
    }

    /** 请求日志详情 */
    @Get("/logs/{id}")
    suspend fun logDetail(@PathVariable id: Long): PlatformLogVO {
        val log = chargeLogic.getPlatformLog(id)
            ?: throw NotFoundException("Log not found: $id")
        val api = apiLogic.get(log.apiId)
        return PlatformLogVO(
            id = log.id,
            apiCode = api?.code,
            apiName = api?.name,
            requestUrl = log.requestUrl,
            requestParams = log.requestParams,
            responseBody = log.responseBody,
            userIp = log.userIp,
            duration = log.duration,
            resultCode = log.resultCode,
            createdAt = log.createdAt
        )
    }

    /** 调用趋势（近 N 天每日统计） */
    @Get("/trend")
    suspend fun trend(
        identity: Identity,
        days: Int = 7
    ): List<AccountTrendVO> {
        val client = clientLogic.getByUserId(identity.id.toLong())
            ?: return emptyList()

        return chargeLogic.pageStats(1, days, clientId = client.id).list.map { stat ->
            AccountTrendVO(
                statDate = stat.statDate,
                callCount = stat.callCount,
                totalCharge = stat.totalCharge
            )
        }
    }

    // ─── helpers ───────────────────────────────────────────────────────────

    private fun maskMiddle(value: String): String {
        if (value.length <= 8) return "****"
        val keep = 4
        return "${value.take(keep)}${"*".repeat(value.length - keep * 2)}${value.takeLast(keep)}"
    }
}
