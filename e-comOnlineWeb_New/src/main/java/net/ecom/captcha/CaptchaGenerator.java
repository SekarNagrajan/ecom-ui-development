/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.captcha;

import java.util.Random;
/**
 *
 * @author gobinath.t
 */
public class CaptchaGenerator {

    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final int CAPTCHA_LENGTH = 5;

    public static String generateCaptcha() {
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();

        captcha.append(UPPERCASE_CHARACTERS.charAt(random.nextInt(UPPERCASE_CHARACTERS.length())));
        captcha.append(LOWERCASE_CHARACTERS.charAt(random.nextInt(LOWERCASE_CHARACTERS.length())));
        captcha.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));

        
        for (int i = 0; i < CAPTCHA_LENGTH - 3; i++) {
            int index = random.nextInt(UPPERCASE_CHARACTERS.length() + LOWERCASE_CHARACTERS.length() + NUMBERS.length());
            if (index < UPPERCASE_CHARACTERS.length()) {
                captcha.append(UPPERCASE_CHARACTERS.charAt(index));
            } else if (index < UPPERCASE_CHARACTERS.length() + LOWERCASE_CHARACTERS.length()) {
                captcha.append(LOWERCASE_CHARACTERS.charAt(index - UPPERCASE_CHARACTERS.length()));
            } else {
                captcha.append(NUMBERS.charAt(index - UPPERCASE_CHARACTERS.length() - LOWERCASE_CHARACTERS.length()));
            }
        }

        
        String shuffledCaptcha = shuffleCaptcha(captcha.toString());

        return shuffledCaptcha;
    }

    
    private static String shuffleCaptcha(String captcha) {
        Random random = new Random();
        char[] captchaChars = captcha.toCharArray();

        for (int i = captchaChars.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = captchaChars[index];
            captchaChars[index] = captchaChars[i];
            captchaChars[i] = temp;
        }

        return new String(captchaChars);
    }
}
