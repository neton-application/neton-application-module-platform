package logic

/**
 * platform 模块的业务错误机器码（spec application/platform-errors）。
 *
 * 规则：异常 / 校验注解 / 限流注解的 `message` 只放这里的机器码（`^[A-Z][A-Z0-9_]{2,}$`），
 * 前端按码翻译（neton-application-front-platform 语言包 `error.<CODE>`）。数字 `code` 取
 * `NetonErrorCode` 的类别；跟请求字段有关的放字段级错误（path = 请求字段，message = code = 机器码）。
 */
object PlatformErrorCodes {
    // ---- 管理端 ----
    /** 更新的接入方（client）不存在。 */
    const val CLIENT_NOT_FOUND = "CLIENT_NOT_FOUND"
    /** 联系人手机号格式不对（字段 contactMobile）。 */
    const val CONTACT_MOBILE_INVALID = "CONTACT_MOBILE_INVALID"

    // ---- 会员端（我的 API 账户）----
    /** 当前账号没有绑定接入方。 */
    const val CLIENT_NOT_BOUND = "CLIENT_NOT_BOUND"
    /** 请求日志不存在，或不属于当前账号的接入方。 */
    const val LOG_NOT_FOUND = "LOG_NOT_FOUND"

    // ---- 开放接口（/order，签名鉴权）----
    /** timestamp 不是整数秒（字段 timestamp）。 */
    const val TIMESTAMP_INVALID = "TIMESTAMP_INVALID"
    /** timestamp 与服务器时间相差超过 300 秒（防重放，字段 timestamp）。 */
    const val TIMESTAMP_EXPIRED = "TIMESTAMP_EXPIRED"
    /** appId 不存在或已停用（字段 appId）。 */
    const val APP_ID_INVALID = "APP_ID_INVALID"
    /** 签名校验失败（字段 signature）。 */
    const val SIGNATURE_INVALID = "SIGNATURE_INVALID"
    /** 查询订单未带 orderId（字段 orderId）。 */
    const val ORDER_ID_REQUIRED = "ORDER_ID_REQUIRED"
    /** 订单不存在。 */
    const val ORDER_NOT_FOUND = "ORDER_NOT_FOUND"
    /** 计费服务未装配。 */
    const val CHARGE_SERVICE_UNAVAILABLE = "CHARGE_SERVICE_UNAVAILABLE"
    /** 订单查询限流。 */
    const val ORDER_QUERY_RATE_LIMITED = "ORDER_QUERY_RATE_LIMITED"
    /** 下单限流。 */
    const val ORDER_CREATE_RATE_LIMITED = "ORDER_CREATE_RATE_LIMITED"
}
