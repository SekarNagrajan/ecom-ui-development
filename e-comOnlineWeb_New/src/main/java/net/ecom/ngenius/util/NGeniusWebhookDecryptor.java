package net.ecom.ngenius.util;

import net.ecom.common.LogUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * Decrypts N-Genius encrypted webhook bodies per:
 * https://docs.ngenius-payments.com/reference/webhook-encryption-decryption-guide
 *
 * Algorithm: AES-256-CBC / PKCS5Padding
 * Encoding: Base64( IV[16 bytes] || ciphertext )
 * Key: exactly 32 ASCII characters (trimmed)
 */
public final class NGeniusWebhookDecryptor {

    private static final String TAG = "[NGeniusWebhookDecryptor]";
    private static final int IV_LENGTH = 16;
    private static final int KEY_LENGTH = 32;
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private NGeniusWebhookDecryptor() {
    }

    /**
     * Returns true when the body looks like plaintext JSON (not Base64 ciphertext).
     */
    public static boolean looksLikePlainJson(String payload) {
        if (payload == null) {
            return false;
        }
        String trimmed = payload.trim();
        return trimmed.startsWith("{") || trimmed.startsWith("[");
    }

    /**
     * Decrypts a Base64-encoded AES-256-CBC webhook body.
     *
     * @param encryptedBase64 body as delivered by N-Genius (Content-Type: text/plain)
     * @param secretKey       32-character ASCII secret from the merchant portal
     * @return decrypted UTF-8 JSON string
     * @throws IllegalArgumentException if key/payload invalid
     * @throws Exception                on crypto failure
     */
    public static String decrypt(String encryptedBase64, String secretKey) throws Exception {
        if (encryptedBase64 == null || encryptedBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Encrypted webhook body is empty");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Webhook encryption secret is not configured");
        }

        String key = secretKey.trim();
        if (key.length() != KEY_LENGTH) {
            throw new IllegalArgumentException(
                    "Webhook encryption secret must be exactly " + KEY_LENGTH
                            + " ASCII characters (got " + key.length() + ")");
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.US_ASCII);
        byte[] decoded = Base64.getDecoder().decode(encryptedBase64.trim());
        if (decoded.length <= IV_LENGTH) {
            throw new IllegalArgumentException(
                    "Encrypted payload too short after Base64 decode (len=" + decoded.length + ")");
        }

        byte[] iv = Arrays.copyOfRange(decoded, 0, IV_LENGTH);
        byte[] cipherBytes = Arrays.copyOfRange(decoded, IV_LENGTH, decoded.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(keyBytes, "AES"),
                new IvParameterSpec(iv));

        byte[] plain = cipher.doFinal(cipherBytes);
        String json = new String(plain, StandardCharsets.UTF_8);
        LogUtil.trace(TAG, "Decrypted webhook payload length=" + json.length());
        return json;
    }
}
