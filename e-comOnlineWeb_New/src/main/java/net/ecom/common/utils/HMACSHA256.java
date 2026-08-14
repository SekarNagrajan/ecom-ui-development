/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common.utils;

/**
 *
 * @author gobinath.t
 */
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class HMACSHA256 {

    private static final String SECRET_KEY = "q8T!xA@Vm72#cR$Jw5LpZgN^bE&Fy9UaHdKsXe0MoYt1BGij"; // Your secret key

    public static String hashWithKey(String input) {
        try {
            // Create a new SecretKeySpec using the secret key
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            // Get a Mac (Message Authentication Code) instance with SHA-256
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);

            // Perform the hashing
            byte[] hashedBytes = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while hashing with HMAC-SHA256", e);
        }
    }

}
