package com.productcnit.service;


import com.productcnit.dto.DecMessage;
import com.productcnit.dto.EncMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.ExemptionMechanismException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import static org.bouncycastle.asn1.x509.ObjectDigestInfo.publicKey;

@Service
public class DecSymService {
    private SecretKey key;
    private int KEY_SIZE = 128;
    private int T_LEN = 128;
    private byte[] IV;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
    }

    public void initFromStrings(String secretKey, String IV){
        key = new SecretKeySpec(decode(secretKey.toString()),"AES");
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

    public DecMessage getDecrypt(String Message,String senderid,
                                 String peerid,String secretkey){

//        String SecretKey = secretkey.replaceAll("^\"|\"$", ""); // Remove leading and trailing quotation marks
        String SecretKey = secretkey;
     initFromStrings(SecretKey, "e3IYYJC2hxe24/EO");

     System.out.println("secretkey1"+secretkey);

        WebClient webClient1 = webClientBuilder.build();
        WebClient webClient2= WebClient.create();

        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8085/Encrypt")
                .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
                .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
                .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
                .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8))
                .build()
                .toUri();

        EncMessage response = webClient2.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(EncMessage.class)
                .block();
        System.out.println("secretkey2"+secretkey);
        String secretMessage = decrypt(response.getMessage().toString());
        System.out.println("secretkey3"+secretkey);

        DecMessage decMessage = new DecMessage();
        decMessage.setMessage(secretMessage);
        decMessage.setSenderId(response.getSenderId());
        decMessage.setRecId(response.getRecId());

        return decMessage;

    }
}
