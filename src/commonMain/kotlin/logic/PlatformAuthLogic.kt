package logic

import model.Client
import table.ClientTable
import neton.database.dsl.*
import neton.logging.Logger
import neton.security.internal.HmacSha256

class PlatformAuthLogic(
    private val log: Logger
) {

    /**
     * Lookup a client by its appId.
     */
    suspend fun lookupClient(appId: String): Client? {
        return ClientTable.oneWhere {
            and(
                Client::appId eq appId,
                Client::status eq 1,
                Client::deleted eq 0
            )
        }
    }

    /**
     * Verify an HMAC-SHA256 signature.
     *
     * The expected signature is computed as HMAC-SHA256(appSecret, stringToSign).
     * The caller constructs stringToSign by sorting request parameters alphabetically
     * and joining them as "key=value&key=value&...&timestamp=<ts>".
     *
     * @param appSecret the client's secret key
     * @param stringToSign the canonical string assembled from request parameters
     * @param signature the signature provided in the request header
     * @return true if the signature matches
     */
    fun verifySignature(appSecret: String, stringToSign: String, signature: String): Boolean {
        val result = HmacSha256.verifyHex(
            secret = appSecret.encodeToByteArray(),
            signingInput = stringToSign.encodeToByteArray(),
            signatureHex = signature
        )
        if (!result) {
            log.info("platform.signature.verify_failed")
        }
        return result
    }
}
