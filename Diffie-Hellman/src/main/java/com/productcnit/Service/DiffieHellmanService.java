package com.productcnit.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class DiffieHellmanService {



    private static final int KEY_SIZE = 2048; // Adjust the key size as needed

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(KEY_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ignored) {
            System.out.println("Error in keypair generation");
            return null;
        }
    }

    public static String generatePublicKey(KeyPair keyPair)  {
        System.out.println(keyPair.getPublic());
        System.out.println(encode(keyPair.getPublic().getEncoded()));
        String publickeyx=Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println(publickeyx);
        byte [] x= Base64.getDecoder().decode(publickeyx);
        System.out.println(x);


    try
    {
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(x);
        System.out.println("step1"+x509KeySpec);
        PublicKey otherPublicKeyObject = keyFactory.generatePublic(x509KeySpec);
        System.out.println("step2"+otherPublicKeyObject);
    } catch (Exception ignored){}


        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public  byte[] decoded(String publickeyx)  {
        System.out.println(publickeyx);
        byte [] x= Base64.getDecoder().decode(publickeyx);
        System.out.println(x);

        return x;
    }



    public static String generateSharedSecret(KeyPair ownKeyPair, String otherPublicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            System.out.println("inputdata"+otherPublicKey);
            byte [] x=  Base64.getDecoder().decode(otherPublicKey);
            System.out.println("stepo" +x);


            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(x);
            System.out.println("step1");
            PublicKey otherPublicKeyObject = keyFactory.generatePublic(x509KeySpec);
            System.out.println("step2");

            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(ownKeyPair.getPrivate());
            System.out.println("step3");
            keyAgreement.doPhase(otherPublicKeyObject, true);

            // Use SecretKeySpec directly
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyAgreement.generateSecret(), 0, 16, "AES");

            return encode(secretKeySpec.getEncoded());
        } catch (Exception e) {
            System.out.println("Error in shared key generation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] decode(String data)
    {
        return  Base64.getDecoder().decode(data);
    }


//    private KeyPair keyPair;
//    private PublicKey client1PublicKey;
//    private PublicKey client2PublicKey;
//
//    public DiffieHellmanService() {
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
//            keyPair = keyPairGenerator.generateKeyPair();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public PublicKey getPublicKey() {
//        return keyPair.getPublic();
//    }
//
//    public String generateSharedKey(String clientPublicKey) {
//        try {
//            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
//            keyAgreement.init(keyPair.getPrivate());
//
//            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(clientPublicKey));
//            KeyFactory keyFactory = KeyFactory.getInstance("DH");
//            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
//
//            keyAgreement.doPhase(publicKey, true);
//
//            byte[] sharedSecret = keyAgreement.generateSecret();
//            return Base64.getEncoder().encodeToString(sharedSecret);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public void setClient1PublicKey(String client1PublicKey) {
//        try {
//            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(client1PublicKey));
//            KeyFactory keyFactory = KeyFactory.getInstance("DH");
//            this.client1PublicKey = keyFactory.generatePublic(x509KeySpec);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setClient2PublicKey(String client2PublicKey) {
//        try {
//            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(client2PublicKey));
//            KeyFactory keyFactory = KeyFactory.getInstance("DH");
//            this.client2PublicKey = keyFactory.generatePublic(x509KeySpec);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public PublicKey getClient1PublicKey() {
//        return client1PublicKey;
//       }
//
//    public PublicKey getClient2PublicKey() {
//        return client2PublicKey;
//    }
}
