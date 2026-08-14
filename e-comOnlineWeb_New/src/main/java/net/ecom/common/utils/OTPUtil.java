package net.ecom.common.utils;

import java.util.Random;

/**
 * Utility class for OTP (One Time Password) generation
 */
public class OTPUtil {

    private static final String DIGITS = "0123456789";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUMERIC_MIXED = DIGITS + UPPERCASE + LOWERCASE;
    private static final Random random = new Random();

  
    /**
     * Generates a random OTP string with alphanumeric characters
     *
     * @param length The length of the OTP to generate
     * @param includeAlphabets Whether to include alphabets in the OTP
     * @return A random OTP string of specified length
     */
    public static String generateOTP(int length, boolean includeAlphabets) {
        if (length <= 0) {
            throw new IllegalArgumentException("OTP length must be greater than 0");
        }

        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALPHANUMERIC_MIXED.length());
            otp.append(ALPHANUMERIC_MIXED.charAt(randomIndex));
        }

        return otp.toString();
    }
}
