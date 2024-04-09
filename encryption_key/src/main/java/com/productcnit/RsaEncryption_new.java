//package com.productcnit;
//
//
//import javax.crypto.Cipher;
//import java.awt.*;
//import java.security.*;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//public class RsaEncryption_new {
//
//    private PublicKey publicKey;
//    private PrivateKey privateKey;
//
//    private static final String PUBLIC_KEY_STRINGS="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";
//    private static final String PRIVATE_KEY_STRINGS="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";
//
//
//
//    public void init()
//    {
//        try
//        {
//            KeyPairGenerator generator= KeyPairGenerator.getInstance("RSA");
//            generator.initialize(1024);
//            KeyPair pair= generator.generateKeyPair();
//            privateKey = pair.getPrivate();
//            publicKey = pair.getPublic();
//        }
//        catch (Exception ignored){}
//    }
//
//    public void initFromStrings()
//    {
//        try
//        {
//            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRINGS));
//            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRINGS));
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            publicKey = keyFactory.generatePublic(keySpecPublic);
//            privateKey = keyFactory.generatePrivate(keySpecPrivate);
//        }
//        catch (Exception ignored){}
//    }
//
//    public void printkeys()
//    {
//        System.err.println("PublicKey:\n"+encode(publicKey.getEncoded()));
//        System.err.println("PrivateKey\n"+encode(privateKey.getEncoded()));
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
//    public String decrypt(String encryptedMessage) throws Exception {
//
//        byte[] encryptedBytes= decode(encryptedMessage);
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.DECRYPT_MODE,privateKey);
//        byte[] decryptedMessages= cipher.doFinal(encryptedBytes);
//        return new String(decryptedMessages,"UTF8");
//
//    }
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
//
//    public static void main(String[] args)
//    {
//        RsaEncryption_new rsaEncryption= new RsaEncryption_new();
//        //rsaEncryption.init();
//        rsaEncryption.initFromStrings();
//        try
//        {
//            String encrptedMessage= rsaEncryption.encrypt("Hello dude");
//            String decrytpedMessage= rsaEncryption.decrypt(encrptedMessage);
//            System.out.println("Then encrypted message\n"+encrptedMessage);
//            System.out.println("Then decrypted message\n"+decrytpedMessage);
//            rsaEncryption.printkeys();
//        }
//        catch (Exception ignored){}
//    }
//
//
//
//}
//
