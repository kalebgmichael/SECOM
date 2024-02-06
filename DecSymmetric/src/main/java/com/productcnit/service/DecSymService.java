package com.productcnit.service;


import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.ExemptionMechanismException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

@Service
public class DecSymService {
    private SecretKey key;
    private int KEY_SIZE = 128;
    private int T_LEN = 128;
    private byte[] IV;

    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
    }

    public void initFromStrings(String secretKey, String IV){
        key = new SecretKeySpec(decode(secretKey),"AES");
        this.IV = decode(IV);
    }

    public String decrypt(String encryptedMessage) {
     try
     {
         byte[] messageInBytes = decode(encryptedMessage);
         Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
         GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
         decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
         byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
         return new String(decryptedBytes);
     }
     catch (Exception ignored)
     {
         return null;
     }
    }

    // signature verification function



    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String getData()
    {
        initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==","e3IYYJC2hxe24/EO");
        try
        {
            WebClient webClient = WebClient.create("http://localhost:8085");

            // Build the request
            WebClient.RequestHeadersSpec<?> requestSpec = webClient
                    .method(HttpMethod.GET)
                    .uri("/getsecret");

            // Execute the request
            Mono<String> responseMono = requestSpec.retrieve().bodyToMono(String.class);

            // Block and get the response
            String response = responseMono.block();
            String encryptedMessage= response.toString();
            // Process the response as needed
//        System.out.println("Response: " + response);
            String secretMessage= decrypt(encryptedMessage);
//            System.err.println("The Secret Message\n"+secretMessage);
            return secretMessage;
        }
        catch (Exception ignored)
        {
            return null;
        }

    }
}
