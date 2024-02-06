package com.productcnit.Controller;


import com.productcnit.Service.DiffieHellmanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RestController
@RequestMapping("/dh-service")
public class DiffieHellmanController {
//    private DiffieHellmanService diffieHellmanService = new DiffieHellmanService();

//    private boolean client1KeySet = false;
//    private boolean client2KeySet = false;
//
//    @PostMapping("/getPublicKey")
//    public String getPublicKey() {
//        return Base64.getEncoder().encodeToString(diffieHellmanService.getPublicKey().getEncoded());
//    }
//
//    @PostMapping("/generateSharedKey")
//    public String generateSharedKey(@RequestBody String clientPublicKey) {
//        return diffieHellmanService.generateSharedKey(clientPublicKey);
//    }
//
//    @PostMapping("/setClient1PublicKey")
//    public void setClient1PublicKey(@RequestBody String client1PublicKey) {
//        diffieHellmanService.setClient1PublicKey(client1PublicKey);
//        client1KeySet = true;
//    }
//
//    @PostMapping("/setClient2PublicKey")
//    public void setClient2PublicKey(@RequestBody String client2PublicKey) {
//        diffieHellmanService.setClient2PublicKey(client2PublicKey);
//        client2KeySet = true;
//    }
//
//    @GetMapping("/initiateKeyExchange")
//    public String initiateKeyExchange() {
//        if (client1KeySet && client2KeySet) {
//            client1KeySet = false;
//            client2KeySet = false;
//
//            // Return a message indicating that the key exchange has started
//            return "Key exchange initiated. Clients can now request each other's public keys.";
//        } else {
//            return "Waiting for both clients to set their public keys.";
//        }
//    }
//
//    @PostMapping("/getClient1PublicKey")
//    public String getClient1PublicKey() {
//        return Base64.getEncoder().encodeToString(diffieHellmanService.getClient1PublicKey().getEncoded());
//    }
//
//    @PostMapping("/getClient2PublicKey")
//    public String getClient2PublicKey() {
//        return Base64.getEncoder().encodeToString(diffieHellmanService.getClient2PublicKey().getEncoded());
//    }


//    @PostMapping("/getPublicKey")
//    public String getPublicKey() {
//        return Base64.getEncoder().encodeToString(diffieHellmanService.getPublicKey().getEncoded());
//    }
//
//    @PostMapping("/generateSharedKey")
//    public String generateSharedKey(@RequestBody String clientPublicKey) {
//        return diffieHellmanService.generateSharedKey(clientPublicKey);
//    }
//
//    @PostMapping("/getClient1PublicKey")
//    public String getClient1PublicKey() {
//        return Base64.getEncoder().encodeToString(diffieHellmanService.getClient1PublicKey().getEncoded());
//    }
//
//    @PostMapping("/setClient1PublicKey")
//    public void setClient1PublicKey(@RequestBody String client1PublicKey) {
//        diffieHellmanService.setClient1PublicKey(client1PublicKey);
//    }


    private final String BASE_URL = "http://localhost:8085/dh-service";
    private final DiffieHellmanService diffieHellmanService;

    private KeyPair serverKeyPair;

    public DiffieHellmanController(DiffieHellmanService diffieHellmanService) {
        this.diffieHellmanService = diffieHellmanService;
    }

    @GetMapping("/initiateKeyExchange")
    public String initiateKeyExchange() throws NoSuchAlgorithmException {
        // Generate the server's key pair
        serverKeyPair = diffieHellmanService.generateKeyPair();

        // Return the server's public key as a Base64-encoded string
        System.out.println(diffieHellmanService.generatePublicKey(serverKeyPair));
        return diffieHellmanService.generatePublicKey(serverKeyPair);
    }


    @GetMapping("/decode")
    public byte[] decoded(String pub) {
        // Clean up the input string (remove whitespaces and newline characters)
        String cleanedPublicKey = pub.replaceAll("\\s", "");

        // Decode the cleaned Base64-encoded string
        byte[] decodedBytes = Base64.getDecoder().decode(cleanedPublicKey);

        return decodedBytes;
    }


    @GetMapping("/exchangeKeys")
    public ResponseEntity<byte[]> exchangeKeys() {
        // Step 1: Call initiateKeyExchange to get the public key
        String publicKey = new RestTemplate().getForObject(BASE_URL + "/initiateKeyExchange", String.class);

        // Step 2: Pass the public key to the decoded endpoint
        ResponseEntity<byte[]> decodedResponse = new RestTemplate().getForEntity(BASE_URL + "/decode?pub={publicKey}", byte[].class, publicKey);

        return decodedResponse;
    }

    @GetMapping("/completeKeyExchange")
    public String completeKeyExchange(String clientPublicKey) throws Exception {
        // Generate the shared secret using the client's public key
        return DiffieHellmanService.generateSharedSecret(serverKeyPair, clientPublicKey);
    }
//
//    @GetMapping("/alice/public-key")
//    public String getAlicePublicKey() {
//        KeyPair aliceKeyPair = diffieHellmanService.generateKeyPair();
//        return diffieHellmanService.generatePublicKey(aliceKeyPair);
//    }
//
//    @GetMapping("/bob/public-key")
//    public String getBobPublicKey() {
//        KeyPair bobKeyPair = diffieHellmanService.generateKeyPair();
//        return diffieHellmanService.generatePublicKey(bobKeyPair);
//    }
//
//    @GetMapping("/alice/shared-secret")
//    public String getAliceSharedSecret(String bobPublicKey) {
//        KeyPair aliceKeyPair = diffieHellmanService.generateKeyPair();
//        return diffieHellmanService.generateSharedSecret(aliceKeyPair, bobPublicKey);
//    }
//
//    @GetMapping("/bob/shared-secret")
//    public String getBobSharedSecret(String alicePublicKey) {
//        KeyPair bobKeyPair = diffieHellmanService.generateKeyPair();
//        return diffieHellmanService.generateSharedSecret(bobKeyPair, alicePublicKey);
//    }
}
