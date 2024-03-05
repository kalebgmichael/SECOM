package com.productcnit.service;

import com.productcnit.dto.GenKeyPairResponse;
import com.productcnit.dto.PublicKeyMessage;
import com.productcnit.dto.PublicKeyMessageSend;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;


@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private static final String PUBLIC_KEY_STRINGS="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";

    @Autowired
    private WebClient.Builder webClientBuilder;
    //function for generating public key for signature
    public PublicKey initFromStringsPublickey(String publickey)
    {
        try
        {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRINGS));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

             return publicKey = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){
            System.out.println("error in initFromStringsPublickey ");
            return null;
        }
    }
    private static final String PRIVATE_KEY_STRINGS="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";


    public PrivateKey initFromStrings(String privatekey)
    {
        try
        {
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(privatekey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

           return privateKey = keyFactory.generatePrivate(keySpecPrivate);
        }
        catch (Exception ignored){
            System.out.println("error in initFromStrings ");
            return null;
        }
    }

    public String decrypt(String encryptedMessage,String privatekey) throws Exception {

        byte[] encryptedBytes= decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,initFromStrings(privatekey));
        byte[] decryptedMessages= cipher.doFinal(encryptedBytes);
        return new String(decryptedMessages,"UTF8");

    }

    public  boolean verifySignature(String message, String signature) throws Exception {
        byte[] messageByte = message.getBytes();
        byte[] signature2 = decode(signature);
        Signature signature1 = Signature.getInstance("SHA256withRSA");
        signature1.initVerify(publicKey);
        signature1.update(messageByte);
        return signature1.verify(signature2);
    }



    private String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data)
    {
        return  Base64.getDecoder().decode(data);
    }


    public String getData(String privatekey)
    {
        System.out.println("privatekey"+privatekey);
        initFromStrings(privatekey);
        try
        {
            WebClient webClient = WebClient.create("http://localhost:8083");

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
            String secretMessage= decrypt(encryptedMessage,privatekey);

            System.out.println("privatekey"+privatekey);
//            System.err.println("The Secret Message\n"+secretMessage);
            return secretMessage;
        }
        catch (Exception ignored)
        {
            return null;
        }

    }

    public GenKeyPairResponse getkeypair(Authentication authentication, String OwnerId)
    {
            WebClient webClient = WebClient.create("http://KEYEX-SERVICE");

            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            System.out.println(jwt.getTokenValue());
            WebClient webClient1 =webClientBuilder.build();
            GenKeyPairResponse response= webClient1.get()
                    .uri("http://KEYEX-SERVICE/api/key/Gengetkeypair"+"/"+OwnerId)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                    .retrieve()
                    .bodyToMono(GenKeyPairResponse.class)
                    .block();

            String privatekey= response.getGen_private_Key();
            String publickey= response.getGen_public_Key();
            String ownerid= response.getGen_Owner_Id();
            String userid= response.getGen_User_Id();
            System.out.println("privatekey"+privatekey);
            System.out.println("publickey"+publickey);
            System.out.println("ownerid"+ownerid);
            System.out.println("userid"+userid);
          ;
//            System.err.println("The Secret Message\n"+secretMessage);
            return response;

    }
    public String Sharedkey(String Recid, String publicKey, Authentication authentication) {
        WebClient webClient = WebClient.create("http://KEYEX-SERVICE");
        System.out.println("publickey"+publicKey);
//        System.out.println("OwnerId"+OwnerId);
//        System.out.println("Recid"+Recid);
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
//        System.out.println(jwt.getTokenValue());

        Map<String, Object> claims = jwt.getClaims();
        String OwnerId = (String) claims.get("Owner_ID");
        System.out.println("OwnerId"+OwnerId);
        String  userId =authentication.getName();

        WebClient webClient1 = webClientBuilder.build();
        String response = webClient1.get()
                .uri(builder -> {
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                            .scheme("http")
                            .host("KEYEX-SERVICE")
                            .path("/api/key/sharedkey")
                            .queryParam("Ownerid", URLEncoder.encode(OwnerId, StandardCharsets.UTF_8))
                            .queryParam("Recid", URLEncoder.encode(Recid, StandardCharsets.UTF_8))
                            .queryParam("publicKey", URLEncoder.encode(publicKey, StandardCharsets.UTF_8));
                    return uriBuilder.build().toUri();
                })
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }


//    public String Sharedkey(String OwnerId,String Recid, String publickey,Authentication authentication)
//    {
//        WebClient webClient = WebClient.create("http://KEYEX-SERVICE");
//
//        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
//        System.out.println(jwt.getTokenValue());
//        WebClient webClient1 =webClientBuilder.build();
//        String response= webClient1.get()
//                .uri("http://KEYEX-SERVICE/api/key/sharedkey?Ownerid={OwnerId}&Recid={Recid}&publicKey={publicKey}")
//                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        return response;
//
//    }


    public boolean getsig()
    {
        initFromStringsPublickey(PUBLIC_KEY_STRINGS);
        try
        {
            WebClient webClient = WebClient.create("http://localhost:8083");

            // Build the request
            WebClient.RequestHeadersSpec<?> requestSpec = webClient
                    .method(HttpMethod.GET)
                    .uri("/getsig");

            // Execute the request
            Mono<String> responseMono = requestSpec.retrieve().bodyToMono(String.class);

            // Block and get the response
            String response = responseMono.block();
            String signaturemessage= response.toString();
            System.out.println(signaturemessage);
            String kalex= "kaleb";
            // Process the response as needed
//        System.out.println("Response: " + response);
            boolean secretMessage= verifySignature(kalex,signaturemessage);
//            System.err.println("The Secret Message\n"+secretMessage);
            return secretMessage;
        }
        catch (Exception ignored)
        {
            System.out.println("error");
            return false;
        }

    }

    public PublicKeyMessage SendPublicKey(PublicKeyMessageSend publicKeyMessage)

    {
        PublicKeyMessage outMessage= new PublicKeyMessage();
        outMessage.setPublicKey(publicKeyMessage.getPublicKey());
        outMessage.setSenderId(publicKeyMessage.getSenderId());
        outMessage.setRecId(publicKeyMessage.getRecId());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));

        messagingTemplate.convertAndSend("/topic/public-key",outMessage);

        return outMessage;
    }


}
