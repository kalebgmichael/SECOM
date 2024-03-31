package com.productcnit;

import com.productcnit.Service.DiffieHellmanService;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.security.*;
import java.util.Base64;

public class DHEX {
    private static final String BASE_URL = "http://localhost:8085/dh-service";
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void init() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        privateKey=pair.getPrivate();
        publicKey=pair.getPublic();

    }
    public void printkeys()
    {
        System.out.println("privatekey\n"+encode(privateKey.getEncoded()));
        System.out.println("publickey\n"+encode(publicKey.getEncoded()));
    }
    public String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] decode(String data)
    {
        return  Base64.getDecoder().decode(data);
    }
    public static void main(String[] args) throws NoSuchAlgorithmException {

        DHEX dhex = new DHEX();
        dhex.init();
        dhex.printkeys();


//        KeyPair keyPair = generateRSAKeyPair(2048);
//        // Print public key and private key in string formats
//        String publicKeyString = convertKeyToString(keyPair.getPublic());
//        String privateKeyString = convertKeyToString(keyPair.getPrivate());
//
//        System.out.println("Public Key:");
//        System.out.println(publicKeyString);
//        System.out.println(publicKeyString.length());
//        System.out.println("\nPrivate Key:");
//        System.out.println(privateKeyString);
//        System.out.println(privateKeyString.length());


        // Alice and Bob are in the same class
//        DiffieHellmanService alice = new DiffieHellmanService();
//        DiffieHellmanService bob = new DiffieHellmanService();
//
//        // Alice and Bob generate their key pairs
//        KeyPair aliceKeyPair = alice.generateKeyPair();
//        KeyPair bobKeyPair = bob.generateKeyPair();
//
//        // Alice and Bob exchange their public keys
//        String alicePublicKey = alice.generatePublicKey(aliceKeyPair);
//        String bobPublicKey = bob.generatePublicKey(bobKeyPair);
//
//        // Alice and Bob generate shared secrets
//        String aliceSharedSecret = alice.generateSharedSecret(aliceKeyPair, bobPublicKey);
//        String bobSharedSecret = bob.generateSharedSecret(bobKeyPair, alicePublicKey);
//
//        // Verify that both Alice and Bob have the same shared secret
//        if (aliceSharedSecret != null && bobSharedSecret != null && aliceSharedSecret.equals(bobSharedSecret)) {
//            System.out.println("Shared secret match: " + aliceSharedSecret);
//        } else {
//            System.out.println("Shared secret mismatch or error occurred.");
//        }
    }
    private static KeyPair generateRSAKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }
    // Convert key to string format
    private static String convertKeyToString(Key key) {
        byte[] keyBytes = key.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }


//    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Client 1 requests its own public key
//        String client1PublicKey = restTemplate.postForObject(BASE_URL + "/getPublicKey", null, String.class);
//        System.out.println("Client 1 Public Key: " + client1PublicKey);
//
//        // Client 1 sends its public key to the server to store
//        restTemplate.postForObject(BASE_URL + "/setClient1PublicKey", client1PublicKey, Void.class);
//
//
//
//        // Wait for the server to notify that key exchange can start
//        String keyExchangeStatus = restTemplate.getForObject(BASE_URL + "/initiateKeyExchange", String.class);
//        System.out.println(keyExchangeStatus);
//
//        // Client 1 requests Client 2's public key
//        String client2PublicKey = restTemplate.postForObject(BASE_URL + "/getClient2PublicKey", null, String.class);
//        System.out.println("Client 2 Public Key: " + client2PublicKey);
//
//        // Shared Key Generation with Client 2
//        String sharedKeyClient1 = restTemplate.postForObject(BASE_URL + "/generateSharedKey", client2PublicKey, String.class);
//        System.out.println("Shared Key Client 1: " + sharedKeyClient1);
//
//    }

//    public static void main(String[] args) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Client 1 requests its own public key
//        String client1PublicKey = restTemplate.postForObject(BASE_URL + "/getPublicKey", null, String.class);
//        System.out.println("Client 1 Public Key: " + client1PublicKey);
//
//        // Client 1 sends its public key to the server to store
//        restTemplate.postForObject(BASE_URL + "/setClient1PublicKey", client1PublicKey, Void.class);
//
//        // Client 1 waits for Client 2 to set its public key
//        // (In a real-world scenario, this would involve some synchronization or signaling)
//
//        // Client 1 requests Client 2's public key
//        String client2PublicKey = restTemplate.postForObject(BASE_URL + "/getClient2PublicKey", null, String.class);
//        System.out.println("Client 2 Public Key: " + client2PublicKey);
//
//        // Shared Key Generation with Client 2
//        String sharedKeyClient1 = restTemplate.postForObject(BASE_URL + "/generateSharedKey", client2PublicKey, String.class);
//        System.out.println("Shared Key Client 1: " + sharedKeyClient1);
//    }
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Client 1
//        String client1PublicKey = restTemplate.postForObject(BASE_URL + "/getPublicKey", null, String.class);
//        System.out.println("Client 1 Public Key: " + client1PublicKey);
//
//        // Client 2
//        String client2PublicKey = restTemplate.postForObject(BASE_URL + "/getPublicKey", null, String.class);
//        System.out.println("Client 2 Public Key: " + client2PublicKey);
//
//        // Shared Key Generation
//        String sharedKeyClient1 = restTemplate.postForObject(BASE_URL + "/generateSharedKey", client2PublicKey, String.class);
//        System.out.println("Shared Key Client 1: " + sharedKeyClient1);
//
//        String sharedKeyClient2 = restTemplate.postForObject(BASE_URL + "/generateSharedKey", client1PublicKey, String.class);
//        System.out.println("Shared Key Client 2: " + sharedKeyClient2);
    //}
}
