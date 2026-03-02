package logic

import model.Client
import table.ClientTable
import neton.database.dsl.*
import neton.logging.Logger

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
                Client::status eq 0,
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
        val expected = hmacSha256(appSecret, stringToSign)
        val result = expected == signature
        if (!result) {
            log.info("Signature verification failed for stringToSign: $stringToSign")
        }
        return result
    }

    /**
     * Compute HMAC-SHA256 and return the hex-encoded result.
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun hmacSha256(key: String, data: String): String {
        val keyBytes = key.encodeToByteArray()
        val dataBytes = data.encodeToByteArray()

        val blockSize = 64
        val paddedKey = if (keyBytes.size > blockSize) {
            sha256(keyBytes)
        } else {
            keyBytes.copyOf(blockSize)
        }

        val ipad = ByteArray(blockSize) { (paddedKey[it].toInt() xor 0x36).toByte() }
        val opad = ByteArray(blockSize) { (paddedKey[it].toInt() xor 0x5c).toByte() }

        val innerHash = sha256(ipad + dataBytes)
        val hmac = sha256(opad + innerHash)

        return hmac.toHexString()
    }

    /**
     * Minimal SHA-256 implementation in pure Kotlin (no platform dependencies).
     */
    private fun sha256(input: ByteArray): ByteArray {
        val k = intArrayOf(
            0x428a2f98.toInt(), 0x71374491, 0xb5c0fbcf.toInt(), 0xe9b5dba5.toInt(),
            0x3956c25b, 0x59f111f1, 0x923f82a4.toInt(), 0xab1c5ed5.toInt(),
            0xd807aa98.toInt(), 0x12835b01, 0x243185be, 0x550c7dc3,
            0x72be5d74, 0x80deb1fe.toInt(), 0x9bdc06a7.toInt(), 0xc19bf174.toInt(),
            0xe49b69c1.toInt(), 0xefbe4786.toInt(), 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152.toInt(), 0xa831c66d.toInt(), 0xb00327c8.toInt(), 0xbf597fc7.toInt(),
            0xc6e00bf3.toInt(), 0xd5a79147.toInt(), 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
            0x650a7354, 0x766a0abb, 0x81c2c92e.toInt(), 0x92722c85.toInt(),
            0xa2bfe8a1.toInt(), 0xa81a664b.toInt(), 0xc24b8b70.toInt(), 0xc76c51a3.toInt(),
            0xd192e819.toInt(), 0xd6990624.toInt(), 0xf40e3585.toInt(), 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
            0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814.toInt(), 0x8cc70208.toInt(),
            0x90befffa.toInt(), 0xa4506ceb.toInt(), 0xbef9a3f7.toInt(), 0xc67178f2.toInt()
        )

        var h0 = 0x6a09e667
        var h1 = 0xbb67ae85.toInt()
        var h2 = 0x3c6ef372
        var h3 = 0xa54ff53a.toInt()
        var h4 = 0x510e527f
        var h5 = 0x9b05688c.toInt()
        var h6 = 0x1f83d9ab
        var h7 = 0x5be0cd19

        val originalLength = input.size.toLong() * 8
        val padded = run {
            val extra = (56 - (input.size + 1) % 64 + 64) % 64
            val buf = ByteArray(input.size + 1 + extra + 8)
            input.copyInto(buf)
            buf[input.size] = 0x80.toByte()
            for (i in 0..7) {
                buf[buf.size - 1 - i] = (originalLength ushr (i * 8)).toByte()
            }
            buf
        }

        fun Int.rotr(n: Int) = (this ushr n) or (this shl (32 - n))

        for (chunkStart in padded.indices step 64) {
            val w = IntArray(64)
            for (i in 0..15) {
                w[i] = ((padded[chunkStart + i * 4].toInt() and 0xff) shl 24) or
                        ((padded[chunkStart + i * 4 + 1].toInt() and 0xff) shl 16) or
                        ((padded[chunkStart + i * 4 + 2].toInt() and 0xff) shl 8) or
                        (padded[chunkStart + i * 4 + 3].toInt() and 0xff)
            }
            for (i in 16..63) {
                val s0 = w[i - 15].rotr(7) xor w[i - 15].rotr(18) xor (w[i - 15] ushr 3)
                val s1 = w[i - 2].rotr(17) xor w[i - 2].rotr(19) xor (w[i - 2] ushr 10)
                w[i] = w[i - 16] + s0 + w[i - 7] + s1
            }

            var a = h0; var b = h1; var c = h2; var d = h3
            var e = h4; var f = h5; var g = h6; var hh = h7

            for (i in 0..63) {
                val s1 = e.rotr(6) xor e.rotr(11) xor e.rotr(25)
                val ch = (e and f) xor (e.inv() and g)
                val temp1 = hh + s1 + ch + k[i] + w[i]
                val s0 = a.rotr(2) xor a.rotr(13) xor a.rotr(22)
                val maj = (a and b) xor (a and c) xor (b and c)
                val temp2 = s0 + maj

                hh = g; g = f; f = e; e = d + temp1
                d = c; c = b; b = a; a = temp1 + temp2
            }

            h0 += a; h1 += b; h2 += c; h3 += d
            h4 += e; h5 += f; h6 += g; h7 += hh
        }

        val result = ByteArray(32)
        intArrayOf(h0, h1, h2, h3, h4, h5, h6, h7).forEachIndexed { i, v ->
            result[i * 4] = (v ushr 24).toByte()
            result[i * 4 + 1] = (v ushr 16).toByte()
            result[i * 4 + 2] = (v ushr 8).toByte()
            result[i * 4 + 3] = v.toByte()
        }
        return result
    }
}
