package init

import neton.core.annotations.Module

/**
 * platform 模块声明锚点（MANIFEST-P1 / P1.1）。
 *
 * KSP 生成 PlatformModuleManifest，聚合：
 * - @Logic 装配（ClientLogic / ApiLogic / ChargeLogic / PlatformAuthLogic）
 * - 手写 runtime（init.PlatformRuntimeBootstrap：Table registry 注册）
 * - 路由（PlatformRouteInitializer）
 * - migrations（init.generated.PlatformMigrationResources）
 */
@Module(dependsOn = ["system"])
object PlatformModule
