/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.SecureRandom;

public final class SunCrypto extends Provider
{
  private static final long serialVersionUID = 6812507587804302833L;
  private static final String info = "SunCrypto Provider (implements RSA, DES, Triple DES, AES, Blowfish, ARCFOUR, RC2, PBE, Diffie-Hellman, HMAC)";
  private static final String OID_PKCS12_RC4_128 = "1.2.840.113549.1.12.1.1";
  private static final String OID_PKCS12_RC4_40 = "1.2.840.113549.1.12.1.2";
  private static final String OID_PKCS12_DESede = "1.2.840.113549.1.12.1.3";
  private static final String OID_PKCS12_RC2_128 = "1.2.840.113549.1.12.1.5";
  private static final String OID_PKCS12_RC2_40 = "1.2.840.113549.1.12.1.6";
  private static final String OID_PKCS5_MD5_DES = "1.2.840.113549.1.5.3";
  private static final String OID_PKCS5_PBKDF2 = "1.2.840.113549.1.5.12";
  private static final String OID_PKCS5_PBES2 = "1.2.840.113549.1.5.13";
  private static final String OID_PKCS3 = "1.2.840.113549.1.3.1";
  static final boolean debug = false;
  private static volatile SunCrypto instance = null;

  static SecureRandom getRandom()
  {
    return SecureRandomHolder.RANDOM;
  }

  public SunCrypto() {
    super("SunCrypto", 1.8D, "SunCrypto Provider (implements RSA, DES, Triple DES, AES, Blowfish, ARCFOUR, RC2, PBE, Diffie-Hellman, HMAC)");

    AccessController.doPrivileged(new PrivilegedAction()
    {
      public Object run()
      {
        SunCrypto.this.put("Cipher.RSA", "com.sun.crypto.provider.RSACipher");
        SunCrypto.this.put("Cipher.RSA SupportedModes", "ECB");
        SunCrypto.this.put("Cipher.RSA SupportedPaddings", "NOPADDING|PKCS1PADDING|OAEPPADDING|OAEPWITHMD5ANDMGF1PADDING|OAEPWITHSHA1ANDMGF1PADDING|OAEPWITHSHA-1ANDMGF1PADDING|OAEPWITHSHA-224ANDMGF1PADDING|OAEPWITHSHA-256ANDMGF1PADDING|OAEPWITHSHA-384ANDMGF1PADDING|OAEPWITHSHA-512ANDMGF1PADDING");

        SunCrypto.this.put("Cipher.RSA SupportedKeyClasses", "java.security.interfaces.RSAPublicKey|java.security.interfaces.RSAPrivateKey");

        SunCrypto.this.put("Cipher.DES", "com.sun.crypto.provider.DESCipher");
        SunCrypto.this.put("Cipher.DES SupportedModes", "ECB|CBC|PCBC|CTR|CTS|CFB|OFB|CFB8|CFB16|CFB24|CFB32|CFB40|CFB48|CFB56|CFB64|OFB8|OFB16|OFB24|OFB32|OFB40|OFB48|OFB56|OFB64");
        SunCrypto.this.put("Cipher.DES SupportedPaddings", "NOPADDING|PKCS5PADDING|ISO10126PADDING");
        SunCrypto.this.put("Cipher.DES SupportedKeyFormats", "RAW");

        SunCrypto.this.put("Cipher.DESede", "com.sun.crypto.provider.DESedeCipher");
        SunCrypto.this.put("Alg.Alias.Cipher.TripleDES", "DESede");
        SunCrypto.this.put("Cipher.DESede SupportedModes", "ECB|CBC|PCBC|CTR|CTS|CFB|OFB|CFB8|CFB16|CFB24|CFB32|CFB40|CFB48|CFB56|CFB64|OFB8|OFB16|OFB24|OFB32|OFB40|OFB48|OFB56|OFB64");
        SunCrypto.this.put("Cipher.DESede SupportedPaddings", "NOPADDING|PKCS5PADDING|ISO10126PADDING");
        SunCrypto.this.put("Cipher.DESede SupportedKeyFormats", "RAW");

        SunCrypto.this.put("Cipher.DESedeWrap", "com.sun.crypto.provider.DESedeWrapCipher");

        SunCrypto.this.put("Cipher.DESedeWrap SupportedModes", "CBC");
        SunCrypto.this.put("Cipher.DESedeWrap SupportedPaddings", "NOPADDING");
        SunCrypto.this.put("Cipher.DESedeWrap SupportedKeyFormats", "RAW");

        SunCrypto.this.put("Cipher.PBEWithMD5AndDES", "com.sun.crypto.provider.PBEWithMD5AndDESCipher");

        SunCrypto.this.put("Alg.Alias.Cipher.OID.1.2.840.113549.1.5.3", "PBEWithMD5AndDES");

        SunCrypto.this.put("Alg.Alias.Cipher.1.2.840.113549.1.5.3", "PBEWithMD5AndDES");

        SunCrypto.this.put("Cipher.PBEWithMD5AndTripleDES", "com.sun.crypto.provider.PBEWithMD5AndTripleDESCipher");

        SunCrypto.this.put("Cipher.PBEWithSHA1AndDESede", "com.sun.crypto.provider.PKCS12PBECipherCore$PBEWithSHA1AndDESede");

        SunCrypto.this.put("Alg.Alias.Cipher.OID.1.2.840.113549.1.12.1.3", "PBEWithSHA1AndDESede");

        SunCrypto.this.put("Alg.Alias.Cipher.1.2.840.113549.1.12.1.3", "PBEWithSHA1AndDESede");

        SunCrypto.this.put("Cipher.PBEWithSHA1AndRC2_40", "com.sun.crypto.provider.PKCS12PBECipherCore$PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("Alg.Alias.Cipher.OID.1.2.840.113549.1.12.1.6", "PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("Alg.Alias.Cipher.1.2.840.113549.1.12.1.6", "PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("Cipher.PBEWithSHA1AndRC2_128", "com.sun.crypto.provider.PKCS12PBECipherCore$PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("Alg.Alias.Cipher.OID.1.2.840.113549.1.12.1.5", "PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("Alg.Alias.Cipher.1.2.840.113549.1.12.1.5", "PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("Cipher.PBEWithSHA1AndRC4_40", "com.sun.crypto.provider.PKCS12PBECipherCore$PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("Alg.Alias.Cipher.OID.1.2.840.113549.1.12.1.2", "PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("Alg.Alias.Cipher.1.2.840.113549.1.12.1.2", "PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("Cipher.PBEWithSHA1AndRC4_128", "com.sun.crypto.provider.PKCS12PBECipherCore$PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("Alg.Alias.Cipher.OID.1.2.840.113549.1.12.1.1", "PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("Alg.Alias.Cipher.1.2.840.113549.1.12.1.1", "PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA1AndAES_128", "com.sun.crypto.provider.PBES2Core$HmacSHA1AndAES_128");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA224AndAES_128", "com.sun.crypto.provider.PBES2Core$HmacSHA224AndAES_128");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA256AndAES_128", "com.sun.crypto.provider.PBES2Core$HmacSHA256AndAES_128");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA384AndAES_128", "com.sun.crypto.provider.PBES2Core$HmacSHA384AndAES_128");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA512AndAES_128", "com.sun.crypto.provider.PBES2Core$HmacSHA512AndAES_128");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA1AndAES_256", "com.sun.crypto.provider.PBES2Core$HmacSHA1AndAES_256");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA224AndAES_256", "com.sun.crypto.provider.PBES2Core$HmacSHA224AndAES_256");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA256AndAES_256", "com.sun.crypto.provider.PBES2Core$HmacSHA256AndAES_256");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA384AndAES_256", "com.sun.crypto.provider.PBES2Core$HmacSHA384AndAES_256");

        SunCrypto.this.put("Cipher.PBEWithHmacSHA512AndAES_256", "com.sun.crypto.provider.PBES2Core$HmacSHA512AndAES_256");

        SunCrypto.this.put("Cipher.Blowfish", "com.sun.crypto.provider.BlowfishCipher");

        SunCrypto.this.put("Cipher.Blowfish SupportedModes", "ECB|CBC|PCBC|CTR|CTS|CFB|OFB|CFB8|CFB16|CFB24|CFB32|CFB40|CFB48|CFB56|CFB64|OFB8|OFB16|OFB24|OFB32|OFB40|OFB48|OFB56|OFB64");
        SunCrypto.this.put("Cipher.Blowfish SupportedPaddings", "NOPADDING|PKCS5PADDING|ISO10126PADDING");
        SunCrypto.this.put("Cipher.Blowfish SupportedKeyFormats", "RAW");

        SunCrypto.this.put("Cipher.AES", "com.sun.crypto.provider.AESCipher$General");
        SunCrypto.this.put("Alg.Alias.Cipher.Rijndael", "AES");
        SunCrypto.this.put("Cipher.AES SupportedModes", "ECB|CBC|PCBC|CTR|CTS|CFB|OFB|CFB8|CFB16|CFB24|CFB32|CFB40|CFB48|CFB56|CFB64|OFB8|OFB16|OFB24|OFB32|OFB40|OFB48|OFB56|OFB64|GCM|CFB72|CFB80|CFB88|CFB96|CFB104|CFB112|CFB120|CFB128|OFB72|OFB80|OFB88|OFB96|OFB104|OFB112|OFB120|OFB128");
        SunCrypto.this.put("Cipher.AES SupportedPaddings", "NOPADDING|PKCS5PADDING|ISO10126PADDING");
        SunCrypto.this.put("Cipher.AES SupportedKeyFormats", "RAW");

        SunCrypto.this.put("Cipher.AES_128/ECB/NoPadding", "com.sun.crypto.provider.AESCipher$AES128_ECB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.1", "AES_128/ECB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.1", "AES_128/ECB/NoPadding");
        SunCrypto.this.put("Cipher.AES_128/CBC/NoPadding", "com.sun.crypto.provider.AESCipher$AES128_CBC_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.2", "AES_128/CBC/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.2", "AES_128/CBC/NoPadding");
        SunCrypto.this.put("Cipher.AES_128/OFB/NoPadding", "com.sun.crypto.provider.AESCipher$AES128_OFB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.3", "AES_128/OFB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.3", "AES_128/OFB/NoPadding");
        SunCrypto.this.put("Cipher.AES_128/CFB/NoPadding", "com.sun.crypto.provider.AESCipher$AES128_CFB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.4", "AES_128/CFB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.4", "AES_128/CFB/NoPadding");
        SunCrypto.this.put("Cipher.AES_128/GCM/NoPadding", "com.sun.crypto.provider.AESCipher$AES128_GCM_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.6", "AES_128/GCM/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.6", "AES_128/GCM/NoPadding");

        SunCrypto.this.put("Cipher.AES_192/ECB/NoPadding", "com.sun.crypto.provider.AESCipher$AES192_ECB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.21", "AES_192/ECB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.21", "AES_192/ECB/NoPadding");
        SunCrypto.this.put("Cipher.AES_192/CBC/NoPadding", "com.sun.crypto.provider.AESCipher$AES192_CBC_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.22", "AES_192/CBC/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.22", "AES_192/CBC/NoPadding");
        SunCrypto.this.put("Cipher.AES_192/OFB/NoPadding", "com.sun.crypto.provider.AESCipher$AES192_OFB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.23", "AES_192/OFB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.23", "AES_192/OFB/NoPadding");
        SunCrypto.this.put("Cipher.AES_192/CFB/NoPadding", "com.sun.crypto.provider.AESCipher$AES192_CFB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.24", "AES_192/CFB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.24", "AES_192/CFB/NoPadding");
        SunCrypto.this.put("Cipher.AES_192/GCM/NoPadding", "com.sun.crypto.provider.AESCipher$AES192_GCM_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.26", "AES_192/GCM/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.26", "AES_192/GCM/NoPadding");

        SunCrypto.this.put("Cipher.AES_256/ECB/NoPadding", "com.sun.crypto.provider.AESCipher$AES256_ECB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.41", "AES_256/ECB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.41", "AES_256/ECB/NoPadding");
        SunCrypto.this.put("Cipher.AES_256/CBC/NoPadding", "com.sun.crypto.provider.AESCipher$AES256_CBC_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.42", "AES_256/CBC/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.42", "AES_256/CBC/NoPadding");
        SunCrypto.this.put("Cipher.AES_256/OFB/NoPadding", "com.sun.crypto.provider.AESCipher$AES256_OFB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.43", "AES_256/OFB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.43", "AES_256/OFB/NoPadding");
        SunCrypto.this.put("Cipher.AES_256/CFB/NoPadding", "com.sun.crypto.provider.AESCipher$AES256_CFB_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.44", "AES_256/CFB/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.44", "AES_256/CFB/NoPadding");
        SunCrypto.this.put("Cipher.AES_256/GCM/NoPadding", "com.sun.crypto.provider.AESCipher$AES256_GCM_NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.46", "AES_256/GCM/NoPadding");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.46", "AES_256/GCM/NoPadding");

        SunCrypto.this.put("Cipher.AESWrap", "com.sun.crypto.provider.AESWrapCipher$General");
        SunCrypto.this.put("Cipher.AESWrap SupportedModes", "ECB");
        SunCrypto.this.put("Cipher.AESWrap SupportedPaddings", "NOPADDING");
        SunCrypto.this.put("Cipher.AESWrap SupportedKeyFormats", "RAW");

        SunCrypto.this.put("Cipher.AESWrap_128", "com.sun.crypto.provider.AESWrapCipher$AES128");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.5", "AESWrap_128");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.5", "AESWrap_128");
        SunCrypto.this.put("Cipher.AESWrap_192", "com.sun.crypto.provider.AESWrapCipher$AES192");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.25", "AESWrap_192");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.25", "AESWrap_192");
        SunCrypto.this.put("Cipher.AESWrap_256", "com.sun.crypto.provider.AESWrapCipher$AES256");
        SunCrypto.this.put("Alg.Alias.Cipher.2.16.840.1.101.3.4.1.45", "AESWrap_256");
        SunCrypto.this.put("Alg.Alias.Cipher.OID.2.16.840.1.101.3.4.1.45", "AESWrap_256");

        SunCrypto.this.put("Cipher.RC2", "com.sun.crypto.provider.RC2Cipher");

        SunCrypto.this.put("Cipher.RC2 SupportedModes", "ECB|CBC|PCBC|CTR|CTS|CFB|OFB|CFB8|CFB16|CFB24|CFB32|CFB40|CFB48|CFB56|CFB64|OFB8|OFB16|OFB24|OFB32|OFB40|OFB48|OFB56|OFB64");
        SunCrypto.this.put("Cipher.RC2 SupportedPaddings", "NOPADDING|PKCS5PADDING|ISO10126PADDING");
        SunCrypto.this.put("Cipher.RC2 SupportedKeyFormats", "RAW");

        SunCrypto.this.put("Cipher.ARCFOUR", "com.sun.crypto.provider.ARCFOURCipher");

        SunCrypto.this.put("Alg.Alias.Cipher.RC4", "ARCFOUR");
        SunCrypto.this.put("Cipher.ARCFOUR SupportedModes", "ECB");
        SunCrypto.this.put("Cipher.ARCFOUR SupportedPaddings", "NOPADDING");
        SunCrypto.this.put("Cipher.ARCFOUR SupportedKeyFormats", "RAW");

        SunCrypto.this.put("KeyGenerator.DES", "com.sun.crypto.provider.DESKeyGenerator");

        SunCrypto.this.put("KeyGenerator.DESede", "com.sun.crypto.provider.DESedeKeyGenerator");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.TripleDES", "DESede");

        SunCrypto.this.put("KeyGenerator.Blowfish", "com.sun.crypto.provider.BlowfishKeyGenerator");

        SunCrypto.this.put("KeyGenerator.AES", "com.sun.crypto.provider.AESKeyGenerator");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.Rijndael", "AES");

        SunCrypto.this.put("KeyGenerator.RC2", "com.sun.crypto.provider.KeyGeneratorCore$RC2KeyGenerator");

        SunCrypto.this.put("KeyGenerator.ARCFOUR", "com.sun.crypto.provider.KeyGeneratorCore$ARCFOURKeyGenerator");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.RC4", "ARCFOUR");

        SunCrypto.this.put("KeyGenerator.HmacMD5", "com.sun.crypto.provider.HmacMD5KeyGenerator");

        SunCrypto.this.put("KeyGenerator.HmacSHA1", "com.sun.crypto.provider.HmacSHA1KeyGenerator");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.OID.1.2.840.113549.2.7", "HmacSHA1");
        SunCrypto.this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.7", "HmacSHA1");

        SunCrypto.this.put("KeyGenerator.HmacSHA224", "com.sun.crypto.provider.KeyGeneratorCore$HmacSHA2KG$SHA224");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.OID.1.2.840.113549.2.8", "HmacSHA224");
        SunCrypto.this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.8", "HmacSHA224");

        SunCrypto.this.put("KeyGenerator.HmacSHA256", "com.sun.crypto.provider.KeyGeneratorCore$HmacSHA2KG$SHA256");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.OID.1.2.840.113549.2.9", "HmacSHA256");
        SunCrypto.this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.9", "HmacSHA256");

        SunCrypto.this.put("KeyGenerator.HmacSHA384", "com.sun.crypto.provider.KeyGeneratorCore$HmacSHA2KG$SHA384");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.OID.1.2.840.113549.2.10", "HmacSHA384");
        SunCrypto.this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.10", "HmacSHA384");

        SunCrypto.this.put("KeyGenerator.HmacSHA512", "com.sun.crypto.provider.KeyGeneratorCore$HmacSHA2KG$SHA512");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.OID.1.2.840.113549.2.11", "HmacSHA512");
        SunCrypto.this.put("Alg.Alias.KeyGenerator.1.2.840.113549.2.11", "HmacSHA512");

        SunCrypto.this.put("KeyPairGenerator.DiffieHellman", "com.sun.crypto.provider.DHKeyPairGenerator");

        SunCrypto.this.put("Alg.Alias.KeyPairGenerator.DH", "DiffieHellman");
        SunCrypto.this.put("Alg.Alias.KeyPairGenerator.OID.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("Alg.Alias.KeyPairGenerator.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("AlgorithmParameterGenerator.DiffieHellman", "com.sun.crypto.provider.DHParameterGenerator");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameterGenerator.DH", "DiffieHellman");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameterGenerator.OID.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameterGenerator.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("KeyAgreement.DiffieHellman", "com.sun.crypto.provider.DHKeyAgreement");

        SunCrypto.this.put("Alg.Alias.KeyAgreement.DH", "DiffieHellman");
        SunCrypto.this.put("Alg.Alias.KeyAgreement.OID.1.2.840.113549.1.3.1", "DiffieHellman");
        SunCrypto.this.put("Alg.Alias.KeyAgreement.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("KeyAgreement.DiffieHellman SupportedKeyClasses", "javax.crypto.interfaces.DHPublicKey|javax.crypto.interfaces.DHPrivateKey");

        SunCrypto.this.put("AlgorithmParameters.DiffieHellman", "com.sun.crypto.provider.DHParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.DH", "DiffieHellman");
        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("AlgorithmParameters.DES", "com.sun.crypto.provider.DESParameters");

        SunCrypto.this.put("AlgorithmParameters.DESede", "com.sun.crypto.provider.DESedeParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.TripleDES", "DESede");

        SunCrypto.this.put("AlgorithmParameters.PBE", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("AlgorithmParameters.PBEWithMD5AndDES", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.5.3", "PBEWithMD5AndDES");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.5.3", "PBEWithMD5AndDES");

        SunCrypto.this.put("AlgorithmParameters.PBEWithMD5AndTripleDES", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("AlgorithmParameters.PBEWithSHA1AndDESede", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.12.1.3", "PBEWithSHA1AndDESede");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.3", "PBEWithSHA1AndDESede");

        SunCrypto.this.put("AlgorithmParameters.PBEWithSHA1AndRC2_40", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.12.1.6", "PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.6", "PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("AlgorithmParameters.PBEWithSHA1AndRC2_128", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.12.1.5", "PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.5", "PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("AlgorithmParameters.PBEWithSHA1AndRC4_40", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.12.1.2", "PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.2", "PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("AlgorithmParameters.PBEWithSHA1AndRC4_128", "com.sun.crypto.provider.PBEParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.12.1.1", "PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.1", "PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("AlgorithmParameters.PBES2", "com.sun.crypto.provider.PBES2Parameters$General");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.OID.1.2.840.113549.1.5.13", "PBES2");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.5.13", "PBES2");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA1AndAES_128", "com.sun.crypto.provider.PBES2Parameters$HmacSHA1AndAES_128");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA224AndAES_128", "com.sun.crypto.provider.PBES2Parameters$HmacSHA224AndAES_128");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA256AndAES_128", "com.sun.crypto.provider.PBES2Parameters$HmacSHA256AndAES_128");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA384AndAES_128", "com.sun.crypto.provider.PBES2Parameters$HmacSHA384AndAES_128");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA512AndAES_128", "com.sun.crypto.provider.PBES2Parameters$HmacSHA512AndAES_128");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA1AndAES_256", "com.sun.crypto.provider.PBES2Parameters$HmacSHA1AndAES_256");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA224AndAES_256", "com.sun.crypto.provider.PBES2Parameters$HmacSHA224AndAES_256");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA256AndAES_256", "com.sun.crypto.provider.PBES2Parameters$HmacSHA256AndAES_256");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA384AndAES_256", "com.sun.crypto.provider.PBES2Parameters$HmacSHA384AndAES_256");

        SunCrypto.this.put("AlgorithmParameters.PBEWithHmacSHA512AndAES_256", "com.sun.crypto.provider.PBES2Parameters$HmacSHA512AndAES_256");

        SunCrypto.this.put("AlgorithmParameters.Blowfish", "com.sun.crypto.provider.BlowfishParameters");

        SunCrypto.this.put("AlgorithmParameters.AES", "com.sun.crypto.provider.AESParameters");

        SunCrypto.this.put("Alg.Alias.AlgorithmParameters.Rijndael", "AES");
        SunCrypto.this.put("AlgorithmParameters.GCM", "com.sun.crypto.provider.GCMParameters");

        SunCrypto.this.put("AlgorithmParameters.RC2", "com.sun.crypto.provider.RC2Parameters");

        SunCrypto.this.put("AlgorithmParameters.OAEP", "com.sun.crypto.provider.OAEPParameters");

        SunCrypto.this.put("KeyFactory.DiffieHellman", "com.sun.crypto.provider.DHKeyFactory");

        SunCrypto.this.put("Alg.Alias.KeyFactory.DH", "DiffieHellman");
        SunCrypto.this.put("Alg.Alias.KeyFactory.OID.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("Alg.Alias.KeyFactory.1.2.840.113549.1.3.1", "DiffieHellman");

        SunCrypto.this.put("SecretKeyFactory.DES", "com.sun.crypto.provider.DESKeyFactory");

        SunCrypto.this.put("SecretKeyFactory.DESede", "com.sun.crypto.provider.DESedeKeyFactory");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.TripleDES", "DESede");

        SunCrypto.this.put("SecretKeyFactory.PBEWithMD5AndDES", "com.sun.crypto.provider.PBEKeyFactory$PBEWithMD5AndDES");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.OID.1.2.840.113549.1.5.3", "PBEWithMD5AndDES");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.5.3", "PBEWithMD5AndDES");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.PBE", "PBEWithMD5AndDES");

        SunCrypto.this.put("SecretKeyFactory.PBEWithMD5AndTripleDES", "com.sun.crypto.provider.PBEKeyFactory$PBEWithMD5AndTripleDES");

        SunCrypto.this.put("SecretKeyFactory.PBEWithSHA1AndDESede", "com.sun.crypto.provider.PBEKeyFactory$PBEWithSHA1AndDESede");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.OID.1.2.840.113549.1.12.1.3", "PBEWithSHA1AndDESede");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.3", "PBEWithSHA1AndDESede");

        SunCrypto.this.put("SecretKeyFactory.PBEWithSHA1AndRC2_40", "com.sun.crypto.provider.PBEKeyFactory$PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.OID.1.2.840.113549.1.12.1.6", "PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.6", "PBEWithSHA1AndRC2_40");

        SunCrypto.this.put("SecretKeyFactory.PBEWithSHA1AndRC2_128", "com.sun.crypto.provider.PBEKeyFactory$PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.OID.1.2.840.113549.1.12.1.5", "PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.5", "PBEWithSHA1AndRC2_128");

        SunCrypto.this.put("SecretKeyFactory.PBEWithSHA1AndRC4_40", "com.sun.crypto.provider.PBEKeyFactory$PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.OID.1.2.840.113549.1.12.1.2", "PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.2", "PBEWithSHA1AndRC4_40");

        SunCrypto.this.put("SecretKeyFactory.PBEWithSHA1AndRC4_128", "com.sun.crypto.provider.PBEKeyFactory$PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.OID.1.2.840.113549.1.12.1.1", "PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.1", "PBEWithSHA1AndRC4_128");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA1AndAES_128", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA1AndAES_128");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA224AndAES_128", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA224AndAES_128");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA256AndAES_128", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA256AndAES_128");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA384AndAES_128", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA384AndAES_128");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA512AndAES_128", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA512AndAES_128");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA1AndAES_256", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA1AndAES_256");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA224AndAES_256", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA224AndAES_256");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA256AndAES_256", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA256AndAES_256");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA384AndAES_256", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA384AndAES_256");

        SunCrypto.this.put("SecretKeyFactory.PBEWithHmacSHA512AndAES_256", "com.sun.crypto.provider.PBEKeyFactory$PBEWithHmacSHA512AndAES_256");

        SunCrypto.this.put("SecretKeyFactory.PBKDF2WithHmacSHA1", "com.sun.crypto.provider.PBKDF2Core$HmacSHA1");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.OID.1.2.840.113549.1.5.12", "PBKDF2WithHmacSHA1");

        SunCrypto.this.put("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.5.12", "PBKDF2WithHmacSHA1");

        SunCrypto.this.put("SecretKeyFactory.PBKDF2WithHmacSHA224", "com.sun.crypto.provider.PBKDF2Core$HmacSHA224");

        SunCrypto.this.put("SecretKeyFactory.PBKDF2WithHmacSHA256", "com.sun.crypto.provider.PBKDF2Core$HmacSHA256");

        SunCrypto.this.put("SecretKeyFactory.PBKDF2WithHmacSHA384", "com.sun.crypto.provider.PBKDF2Core$HmacSHA384");

        SunCrypto.this.put("SecretKeyFactory.PBKDF2WithHmacSHA512", "com.sun.crypto.provider.PBKDF2Core$HmacSHA512");

        SunCrypto.this.put("Mac.HmacMD5", "com.sun.crypto.provider.HmacMD5");
        SunCrypto.this.put("Mac.HmacSHA1", "com.sun.crypto.provider.HmacSHA1");
        SunCrypto.this.put("Alg.Alias.Mac.OID.1.2.840.113549.2.7", "HmacSHA1");
        SunCrypto.this.put("Alg.Alias.Mac.1.2.840.113549.2.7", "HmacSHA1");
        SunCrypto.this.put("Mac.HmacSHA224", "com.sun.crypto.provider.HmacCore$HmacSHA224");

        SunCrypto.this.put("Alg.Alias.Mac.OID.1.2.840.113549.2.8", "HmacSHA224");
        SunCrypto.this.put("Alg.Alias.Mac.1.2.840.113549.2.8", "HmacSHA224");
        SunCrypto.this.put("Mac.HmacSHA256", "com.sun.crypto.provider.HmacCore$HmacSHA256");

        SunCrypto.this.put("Alg.Alias.Mac.OID.1.2.840.113549.2.9", "HmacSHA256");
        SunCrypto.this.put("Alg.Alias.Mac.1.2.840.113549.2.9", "HmacSHA256");
        SunCrypto.this.put("Mac.HmacSHA384", "com.sun.crypto.provider.HmacCore$HmacSHA384");

        SunCrypto.this.put("Alg.Alias.Mac.OID.1.2.840.113549.2.10", "HmacSHA384");
        SunCrypto.this.put("Alg.Alias.Mac.1.2.840.113549.2.10", "HmacSHA384");
        SunCrypto.this.put("Mac.HmacSHA512", "com.sun.crypto.provider.HmacCore$HmacSHA512");

        SunCrypto.this.put("Alg.Alias.Mac.OID.1.2.840.113549.2.11", "HmacSHA512");
        SunCrypto.this.put("Alg.Alias.Mac.1.2.840.113549.2.11", "HmacSHA512");

        SunCrypto.this.put("Mac.HmacPBESHA1", "com.sun.crypto.provider.HmacPKCS12PBESHA1");

        SunCrypto.this.put("Mac.PBEWithHmacSHA1", "com.sun.crypto.provider.PBMAC1Core$HmacSHA1");

        SunCrypto.this.put("Mac.PBEWithHmacSHA224", "com.sun.crypto.provider.PBMAC1Core$HmacSHA224");

        SunCrypto.this.put("Mac.PBEWithHmacSHA256", "com.sun.crypto.provider.PBMAC1Core$HmacSHA256");

        SunCrypto.this.put("Mac.PBEWithHmacSHA384", "com.sun.crypto.provider.PBMAC1Core$HmacSHA384");

        SunCrypto.this.put("Mac.PBEWithHmacSHA512", "com.sun.crypto.provider.PBMAC1Core$HmacSHA512");

        SunCrypto.this.put("Mac.SslMacMD5", "com.sun.crypto.provider.SslMacCore$SslMacMD5");

        SunCrypto.this.put("Mac.SslMacSHA1", "com.sun.crypto.provider.SslMacCore$SslMacSHA1");

        SunCrypto.this.put("Mac.HmacMD5 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.HmacSHA1 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.HmacSHA224 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.HmacSHA256 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.HmacSHA384 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.HmacSHA512 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.HmacPBESHA1 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.PBEWithHmacSHA1 SupportedKeyFormatS", "RAW");
        SunCrypto.this.put("Mac.PBEWithHmacSHA224 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.PBEWithHmacSHA256 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.PBEWithHmacSHA384 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.PBEWithHmacSHA512 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.SslMacMD5 SupportedKeyFormats", "RAW");
        SunCrypto.this.put("Mac.SslMacSHA1 SupportedKeyFormats", "RAW");

        SunCrypto.this.put("KeyStore.JCEKS", "com.sun.crypto.provider.JceKeyStore");

        SunCrypto.this.put("KeyGenerator.SunTlsPrf", "com.sun.crypto.provider.TlsPrfGenerator$V10");

        SunCrypto.this.put("KeyGenerator.SunTls12Prf", "com.sun.crypto.provider.TlsPrfGenerator$V12");

        SunCrypto.this.put("KeyGenerator.SunTlsMasterSecret", "com.sun.crypto.provider.TlsMasterSecretGenerator");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.SunTls12MasterSecret", "SunTlsMasterSecret");

        SunCrypto.this.put("KeyGenerator.SunTlsKeyMaterial", "com.sun.crypto.provider.TlsKeyMaterialGenerator");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.SunTls12KeyMaterial", "SunTlsKeyMaterial");

        SunCrypto.this.put("KeyGenerator.SunTlsRsaPremasterSecret", "com.sun.crypto.provider.TlsRsaPremasterSecretGenerator");

        SunCrypto.this.put("Alg.Alias.KeyGenerator.SunTls12RsaPremasterSecret", "SunTlsRsaPremasterSecret");

        return null;
      }
    });
    if (instance == null)
      instance = this;
  }

  static SunCrypto getInstance()
  {
    if (instance == null) {
      return new SunCrypto();
    }
    return instance;
  }

  private static class SecureRandomHolder
  {
    static final SecureRandom RANDOM = new SecureRandom();
  }
}