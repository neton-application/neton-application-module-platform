package error

import contract.testing.ErrorMessageGuard
import kotlin.test.Test

/** platform 模块的业务错误 message 必须是 [logic.PlatformErrorCodes] 里的机器码。 */
class ErrorMessageGuardTest {
    @Test
    fun businessErrorMessagesAreMachineCodes() {
        ErrorMessageGuard.assertModuleClean(allowlist = emptySet())
    }
}
