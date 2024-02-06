//package com.productcnit;
//
//import javax.crypto.Cipher;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.util.Base64;
//
//public class RsaEncryption {
//
//    private PublicKey publicKey;
//    private PrivateKey privateKey;
//
//    public RsaEncryption()
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
//        RsaEncryption rsaEncryption= new RsaEncryption();
//        try
//        {
//          String encrptedMessage= rsaEncryption.encrypt("Hello dude");
//          String decrytpedMessage= rsaEncryption.decrypt(encrptedMessage);
//          System.out.println("Then encrypted message\n"+encrptedMessage);
//          System.out.println("Then decrypted message\n"+decrytpedMessage);
//        }
//        catch (Exception ignored){}
//    }
//
//
//
//}
