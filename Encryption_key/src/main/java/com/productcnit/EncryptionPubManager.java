//package com.productcnit;
//
//import javax.crypto.Cipher;
//import java.security.KeyFactory;
//import java.security.PublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//public class EncryptionPubManager {
//
//    private PublicKey publicKey;
//    private static final String PUBLIC_KEY_STRINGS="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";
//
//    public void initFromStrings()
//    {
//        try
//        {
//            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRINGS));
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            publicKey = keyFactory.generatePublic(keySpecPublic);
//        }
//        catch (Exception ignored){}
//    }
//
//    public String encrypt(String message) throws Exception
//    {
//        byte[] messageTobytes= message.getBytes();
//        Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
//        byte[] encryptedbytes= cipher.doFinal(messageTobytes);
//        return encode(encryptedbytes);
//
//    }
//
//
//    private String encode(byte[] data)
//    {
//        return Base64.getEncoder().encodeToString(data);
//    }
//
//    private byte[] decode(String data)
//    {
//        return  Base64.getDecoder().decode(data);
//    }
//}
