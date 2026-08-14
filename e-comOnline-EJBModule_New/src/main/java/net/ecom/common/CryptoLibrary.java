/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CryptoLibrary {

    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private BASE64Encoder encoder = new BASE64Encoder();
    private BASE64Decoder decoder = new BASE64Decoder();

    public CryptoLibrary() throws SecurityException {
        Security.addProvider(new SunCrypto());
        char[] pass = "CHANGE THIS TO A BUNCH OF RANDOM CHARACTERS".toCharArray();

        byte[] salt = {-93, 33, 36, 44, -14, -46, 62, 25};

        int iterations = 3;
        init(pass, salt, iterations);
    }

    public void init(char[] pass, byte[] salt, int iterations) throws SecurityException {
        try {
            PBEParameterSpec ps = new PBEParameterSpec(salt, 20);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey k = kf.generateSecret(new PBEKeySpec(pass));

            this.encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            this.encryptCipher.init(1, k, ps);

            this.decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            this.decryptCipher.init(2, k, ps);
        } catch (Exception e) {
            throw new SecurityException("Could not initialize CryptoLibrary: " + e.getMessage());
        }
    }

    public synchronized String encrypt(String str) throws SecurityException {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = this.encryptCipher.doFinal(utf8);
            return this.encoder.encode(enc);
        } catch (Exception e) {
            System.out.println("[ECOM] CryptoLibrary could not encrypt");
            e.printStackTrace();
        }
        throw new SecurityException("Could not encrypt: ");
    }

    public synchronized String decrypt(String str) throws SecurityException {
        try {
            byte[] dec = this.decoder.decodeBuffer(str);
            byte[] utf8 = this.decryptCipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            System.out.println("[ECOM] CryptoLibrary decrypt Could not decrypt: ");
        }
        throw new SecurityException("Could not decrypt: ");
    }

}
