package init

import infra.TableRegistryBuilder
import neton.core.component.NetonContext
import init.generated.PlatformMigrationResources
import neton.core.module.MigrationSource
import neton.core.module.ModuleInitializer
import neton.logging.LoggerFactory

import model.*
import table.*
import logic.*

object PlatformModuleInitializer : ModuleInitializer {

    override val moduleId: String = "platform"
    override val dependsOn: List<String> = listOf("system")

    /** SQL embed 到 binary,由 build.gradle.kts `generateMigrationResources` task 生成. */
    override fun migrations(): List<MigrationSource> = PlatformMigrationResources.sources

    override fun initialize(ctx: NetonContext) {
        val loggerFactory = ctx.get(LoggerFactory::class)
        val registry = ctx.get(TableRegistryBuilder::class)

        // 注册 Table
        registry.register(Client::class, ClientTable)
        registry.register(Api::class, ApiTable)
        registry.register(ClientApi::class, ClientApiTable)
        registry.register(ChargeRecord::class, ChargeRecordTable)
        registry.register(PlatformLog::class, PlatformLogTable)
        registry.register(PlatformStat::class, PlatformStatTable)

        // 绑定 Logic
        ctx.bind(ClientLogic::class, ClientLogic(loggerFactory.get("logic.platform-client")))
        ctx.bind(ApiLogic::class, ApiLogic(loggerFactory.get("logic.platform-api")))
        ctx.bind(ChargeLogic::class, ChargeLogic(loggerFactory.get("logic.platform-charge")))
        ctx.bind(PlatformAuthLogic::class, PlatformAuthLogic(loggerFactory.get("logic.platform-auth")))

        // 注册 KSP 生成的路由
        neton.module.platform.generated.PlatformRouteInitializer.initialize(ctx)
    }
}
