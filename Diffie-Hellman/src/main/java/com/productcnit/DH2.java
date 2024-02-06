package com.productcnit;

import org.springframework.web.client.RestTemplate;

public class DH2 {

//    private static final String BASE_URL = "http://localhost:8085/dh-service";
//
//    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Client 2 requests its own public key
//        String client2PublicKey = restTemplate.postForObject(BASE_URL + "/getPublicKey", null, String.class);
//        System.out.println("Client 2 Public Key: " + client2PublicKey);
//
//        // Client 2 sends its public key to the server to store
//        restTemplate.postForObject(BASE_URL + "/setClient2PublicKey", client2PublicKey, Void.class);
//
//        // Wait for the server to notify that key exchange can start
//        String keyExchangeStatus = restTemplate.getForObject(BASE_URL + "/initiateKeyExchange", String.class);
//        System.out.println(keyExchangeStatus);
//
//        // Client 2 requests Client 1's public key
//        String client1PublicKey = restTemplate.postForObject(BASE_URL + "/getClient1PublicKey", null, String.class);
//        System.out.println("Client 1 Public Key: " + client1PublicKey);
//
//        // Shared Key Generation with Client 1
//        String sharedKeyClient2 = restTemplate.postForObject(BASE_URL + "/generateSharedKey", client1PublicKey, String.class);
//        System.out.println("Shared Key Client 2: " + sharedKeyClient2);
//    }
}