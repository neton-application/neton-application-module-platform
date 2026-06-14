package init

import infra.TableRegistryBuilder
import neton.core.component.NetonContext
import model.*
import table.*

// MANIFEST-P1: 手写 runtime bootstrap。moduleId/dependsOn/migrations 已移到
// init.PlatformModule (@Module 锚点) + KSP manifest; @Logic 装配 (ClientLogic /
// ApiLogic / ChargeLogic / PlatformAuthLogic) 由生成的 PlatformLogicInitializer 处理;
// 路由由 manifest 调 PlatformRouteInitializer。这里只剩 Table registry 注册
// (registry.register 副作用, 非 @Logic 机制)。
object PlatformRuntimeBootstrap {

    fun initialize(ctx: NetonContext) {
        val registry = ctx.get(TableRegistryBuilder::class)

        // 注册 Table
        registry.register(Client::class, ClientTable)
        registry.register(Api::class, ApiTable)
        registry.register(ClientApi::class, ClientApiTable)
        registry.register(ChargeRecord::class, ChargeRecordTable)
        registry.register(PlatformLog::class, PlatformLogTable)
        registry.register(PlatformStat::class, PlatformStatTable)
    }
}
