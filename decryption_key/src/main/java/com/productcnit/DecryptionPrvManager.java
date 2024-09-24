//package com.productcnit;
//
//import org.springframework.http.HttpMethod;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import javax.crypto.Cipher;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//public class DecryptionPrvManager {
//
//    private PrivateKey privateKey;
//
//    private static final String PRIVATE_KEY_STRINGS="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";
//
//    public void initFromStrings()
//    {
//        try
//        {
//            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRINGS));
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            privateKey = keyFactory.generatePrivate(keySpecPrivate);
//        }
//        catch (Exception ignored){}
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
//    public static void main(String[] args) {
//        DecryptionPrvManager decryptionPrvManager = new DecryptionPrvManager();
//        decryptionPrvManager.initFromStrings();
//
//        try
//        {
//            WebClient webClient = WebClient.create("http://localhost:8083");
//
//            // Build the request
//            WebClient.RequestHeadersSpec<?> requestSpec = webClient
//                    .method(HttpMethod.GET)
//                    .uri("/getsecret");
//
//            // Execute the request
//            Mono<String> responseMono = requestSpec.retrieve().bodyToMono(String.class);
//
//            // Block and get the response
//            String response = responseMono.block();
//            String encryptedMessage= response.toString();
//            // Process the response as needed
////        System.out.println("Response: " + response);
//            String secretMessage= decryptionPrvManager.decrypt(encryptedMessage);
//            System.err.println("The Secret Message\n"+secretMessage);
//        }
//        catch (Exception ignored)
//        {
//
//        }
//
//
//    }
//
//}
