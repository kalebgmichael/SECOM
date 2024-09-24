package com.productcnit.service;

import com.productcnit.dto.*;
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
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
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

    private static final String PUBLIC_KEY_STRINGS1="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";

    private static final String PUBLIC_KEY_STRINGS2= "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDAhckA35Q3jrmXGmKuDlRS4IVt06uLn4GhjDGP0Dj70u8fUTq9lH1DLGh6E+H7g/rd7hEJVh9h3U2nAyjWMDsT0Qk3RPnCt1Ld8V/YY0rB62BEhcCVBFMTxqEmhMnsIP4Er2goeEpS7wfFKz97QZbWJqj60HzIUmGf65pauRiM+MkhqNDtA8FoOeM2c+AoovHXRSEw1P65GpHEE2IWJJTm7SXcvTPboyn/z78VzvzD31iBbEY8e3eitN7jhp03GjK8jrPALMq3d/as+O2UOXSc01Gn75BsYG0gvKRZxHfO7FfF8L2vkJ4wAqBxj+XMllUXrH1S1RgSd2q0Q4rK0N3/AgMBAAECggEABvUOutGu6S8RioyxN48HAb5hXq2r4QN7oMXl3fJCt5RQ1eCOmd6YLumXlknKUdeGz6b/G0eZzLnoSM/MFalv3nB1v8yQpgu6mpCoIQloNd0hVniVlA0Fn4z/B9r78mTi08fZoL9wGCfRL7viCGc/Ydp1zX05SoKc9TWDbMyMPukykhi2Cyyn61fFteiiczXGFAFA86G7vryS+XD53rk9GsV45fl0gkWAATieV9Z/IE4fsOKAzC0q07Z+0Ryn+gTwSWOJphP2Ghy4dAqgMUSQ1tLYSXxZlILvtfvkM5pNMji1DnvZgCv4y6D1hBJx/ZrOViz4jYJY1JyKuspfFeWEOQKBgQDyx7YYMg2efOvznciG7Alc/OxnPDVOnQ50KxNb/IcDZYOXM6wdrkMcKW7oo3CxDVhKWHxDyAVPDhWdU/HSeYqHp2qQ+N8BCA66O+v/IzfowKl/BFKSG1hiQEqIcwI4X74w3vQn/LfYlzHGrBJ2N49JKyJohrRufVKxmN77AM1DVQKBgQDLAX9oSRmd5JmYXSUYLnzVaIZh30qdNu2OeyLCkxBq6k7k3slnowPt1gXMarYI+BOvQOCM9OE0xaAZgDeIxE7jOC6nIDLH+9oYlJrCkTU0Aq24X3IlPrU4I+cU26SUe58UN7TG17D/SiaWtubR1UrvbOule1tkaFpM3r3lVWzEAwKBgQDyGTc7z4YHOplsWTZjXTNFGC6CD+c0mqeULnRisWLNf0iG2g9Tlbf0eFjSAlHZLO6TTMO2L9sjZ28kcVZfRbZf6hH5uUNgWvGcwy5mQW96Wc1sDRpt+njbmTL5+H4JS2h0gZBF49xNTxBjBqpuWp83Hp10UIfS+oQg98m70aPe9QKBgEBmCzrT5bJ28FqrQbc4GBXDAM5PfWX+JMIMQeyjppOkHL7yBcZAqmBqgpBkUbbqq6Pu2s0caczJ3I36ZKOYUsY17mbKChHyzS77BVUvp0cGioFwlc9G40oZ6ZZuTvljdgV93nUtu1Jg/XjWMMbj+M+XJ/Ho/gcOK4czKd50iPptAoGAPsjGRReyx6RyYCI+N/zpco4i3H1lL8cMfQZkvpHpU1SWcJP4g/iSDPoVatzBx+StCzipmxx7RfDXsIxY/RxeIPb6p4NiEl74DQ1nJWfDpLoVe2F5uof5nZtDtrJMJCVhFrLxOE0TX5ZvLNUwYh6QWydLW24jMv+COUFmlxDN1P8=";
    private static final String PUBLIC_KEY_STRINGS_last= "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAsSmzXPkrw27bTnjkPL9YadgxPV6QaDD1pPIRaoGR/NTB18OFIbaYRBhindijU7R9cvmwb7cVbtL+sOSZ6XguEmwX8POYZaXywf4IujD9LE0/Ja1JvciRULqVIWdxRghQdLWvdIh+sqkBlcvgwieUvr8xX/B+nFCPdJSSCcWqIb/R620HHe3Qg8t5ToxR94kYFO8vts3uUBy56p0hitfYyEiniB2503B4L0/CPMVTuyKMlHwQPaJZJ2QKz18GubuWLmZzCCGeYkuY2nq9Sj1MF7VUftS7vPgd9ArcfiWfMqZO4Ec9QNcpWPffrN1YmGUB9JxNMgFzEHGPodkD1G68WkhZY1z0PgFgfAaVYw0AovWsEMl9/KBJGiHnUScVLri1o8UdYPMkOnkiSisz3f68N4bAle4CT82pLt/MAGQ9Yn0LndULQLazPqs0IjffWKeD6LZaw/JZjpSYblXrdV0N2b4AXlXLmLdo9BhKTKRXwhwWsybggFlQioGxMvz8FKNpP7hwqNyChUbMnqytKWjdynlFpvazAlQ3tvq4bGWFhEMzDci0tgvkOKlyHrUBsVbwg5tQscg3yc3fbx7ojKTCFY+CnZ/X/iY7uMP/Pn6GPQsVDyxqMexNKdRmKGqQy+WR4X+R8xZzi7Zlg+k9MWKoqjfEd4kBgz4lrbAunW3ENskCAwEAAQ==";

    private static final String PUBLIC_KEY_STRINGS="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmE12WPyKfXoAuqHPWabgWbH5ZzEk2mlQWx/IgkFyqDjOKAlochyntt6V/cXo/H4HtIAOcrkCjkoZpnJD8fN8OkXLMD5R3AlK0/HZFaKBypcVz6gpZnM72PiJk/gWd5sBSmenGiK0q8tSCluV5+KN1zppO0BKIt91q9EFEJH9IeYRiADic+qTjgp6ENxKXNacvNNiTSopZFpMpBVy+IyNlYc84cOIhMhifLxEyrB8AcO7C3QnpgBsjaiBO11CEuDsrC33Fd6DTZeh+8CKdClGuVG6JejB+yOXhnvaHQnXbM4cvTf0Yz10doDe+rEyWugoi29iJBnkmrOiBCWcVh0MmQIDAQAB";
    private static final String PRIVATE_KEY_STRINGS= "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYTXZY/Ip9egC6oc9ZpuBZsflnMSTaaVBbH8iCQXKoOM4oCWhyHKe23pX9xej8fge0gA5yuQKOShmmckPx83w6RcswPlHcCUrT8dkVooHKlxXPqClmczvY+ImT+BZ3mwFKZ6caIrSry1IKW5Xn4o3XOmk7QEoi33Wr0QUQkf0h5hGIAOJz6pOOCnoQ3Epc1py802JNKilkWkykFXL4jI2Vhzzhw4iEyGJ8vETKsHwBw7sLdCemAGyNqIE7XUIS4OysLfcV3oNNl6H7wIp0KUa5Ubol6MH7I5eGe9odCddszhy9N/RjPXR2gN76sTJa6CiLb2IkGeSas6IEJZxWHQyZAgMBAAECggEAO9MZrMSuZbFZvfW3bCDXuYPmUrmEH2ME1HZ8nWW18ww1NpbTdxd4baWrRz7JUPz0a9ttUXUI4vmKb84skEqzPnSR5sZkA0mHIg+61Pu7feexApusf3i+RnECIaRuttalQCPNCMAfNegstrLZBHgCfUuNcF/Ff0NFhpMZKTQfOYP5s54r/rPcWAiHHQfQeVi3D6auIvdjmpFxfCJZ1d72/T4EpBQskcpcwBZ7MpgqLJosLRHJ1AyFXs0lWLC9bfHE+seieSOXy788jWwB1AAsQy7971d9LWf4hRhxYXM0+vHniN3CChzQfpdgF3Fgv4zioaf9XphPSJwD93Lex86FHQKBgQDIsC6oTziWBJFzoVTbNx5sCCA8xzSqsdBGcIMP0xQ5hnrVekZ4IqoZe5hwE9k/hkC2UFerW5yPKN5iAuhmiPN5F3eYIewRyR4d49vkrQ3DpjjJsGgPSqWQIZIn60lIbmAKqzKsMM2riaBCf13h0a1lhGdCaSoKf0YBx8eIt0tjewKBgQDCR16On2lGrfNFVCmiEj8SnsjoOqybOn1BSR+CYTLJx3VRrLa0ncF/gWlAEuIMVXDkVuQ1/CTIco62W6EPOSWUJ0mYksTrGtq1x3oM7nUomzwDHxTLqTYkJJewfCaVmHDZqvGIhN4fWTap6yLdAxetDgbFcFEjxsSk4pxJbtqZ+wKBgQCabjDJJYUWs9c1kn0Y33ZSjYI5ItWdUI1D1wqTn8sdZSWapqgZhBs+1sP74to3LtkXMgX0f9+erkVKSJCLbnFkyjEmaVweZ5P8Tj4E30ILPxrN4SiCM9/pZFvyyfSvfvf63bh1lmx9xr8CzL3yqoXy64sGKKTIRR4a+p/Jzbp+hwKBgDBFw3z0pxfkyPmE/ANFPV7HDWyGh7TaR50dKjMfK5tvb/snSw591EJ2SUyeZIRkBnT2EiZHzGvx6vvssIwzyt6RFHxQm5U/SvC369+isIGycKrl4ijolodMgFRdxMiO5BtxmiotuXNYm3iE8FiyGAPr5ErSqZaWIO5HUEX+wVpZAoGAVn8aI93ksxkDageRNm1rByCFjNHGqv/vMuB+hoQe3fUBZgOZPaY2XpO2WQysutQ4UGY7iV47jCEW9xfC0oI74OuWFJ+KFSmoPOwtAfhPmJj4b3K1DVg/R45mbdr8wgT1SYpRr0ZeGajKnVALxOQjy6k962LNOFXrlg+lP0+mSog=";
    @Autowired
    private WebClient.Builder webClientBuilder;

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

    public String decrypt_symmetric(String encryptedMessage) {
        try
        {
            byte[] messageInBytes = decode(encryptedMessage);
            System.out.println("messageInBytes"+messageInBytes);
            Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
            decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
            System.out.println("decrypted bytes"+new String(decryptedBytes));
            return new String(decryptedBytes);
        }
        catch (Exception ignored)
        {
            return null;
        }
    }
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
   // private static final String PRIVATE_KEY_STRINGS="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";


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

    public String getUser_ca()
    {
        //String PUBLIC_KEY_STRINGS="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";
        return PUBLIC_KEY_STRINGS;
    }
    public String getUser_prvkey()
    {
      String PRIVATE_KEY_STRINGS="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";
      return PRIVATE_KEY_STRINGS;
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
    public String Sharedkey(String Recid, String SenderId, String publicKey, Authentication authentication) {
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
                            .queryParam("SenderId", URLEncoder.encode(SenderId, StandardCharsets.UTF_8))
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
    public DecMessage getDecrypt(String Message,String senderid,
                                 String peerid,String secretkey,Authentication authentication){

        String SecretKey = secretkey;
        initFromStrings(SecretKey, "e3IYYJC2hxe24/EO");
        WebClient webClient2 = webClientBuilder.build();
        WebClient webClient1= WebClient.create();

//        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8085/Encrypt")
//                .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
//                .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
//                .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
//                .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8))
//                .build()
//                .toUri();
//
//        EncMessage response = webClient2.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(EncMessage.class)
//                .block();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        EncMessage response = webClient2.get()
                .uri(builder -> {
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                            .scheme("http")
                            .host("ENCSYM-SERVICE")
                            .path("/api/encsym/Encrypt")
                            .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
                            .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
                            .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
                            .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8));
                    return uriBuilder.build().toUri();
                })
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(EncMessage.class)
                .block();

        String secretMessage = decrypt_symmetric(response.getMessage().toString());
        DecMessage decMessage = new DecMessage();
        decMessage.setMessage(secretMessage);
        decMessage.setSenderId(response.getSenderId());
        decMessage.setRecId(response.getRecId());

        return decMessage;

    }

    public EncMessageResponse getEncrypt(String Message,String senderid,
                                 String peerid,String secretkey, Authentication authentication){

        String SecretKey = secretkey;
        WebClient webClient2 = webClientBuilder.build();
        WebClient webClient1= WebClient.create();

//        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8085/Encrypt")
//                .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
////                .queryParam("message",Message)
//                .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
//                .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
//                .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8))
//                .build()
//                .toUri();
//
//        EncMessage response = webClient2.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(EncMessage.class)
//                .block();

        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        EncMessage response = webClient2.get()
                .uri(builder -> {
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                            .scheme("http")
                            .host("ENCSYM-SERVICE")
                            .path("/api/encsym/Encrypt")
                            .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
                            .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
                            .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
                            .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8));
                    return uriBuilder.build().toUri();
                })
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(EncMessage.class)
                .block();

        EncMessageResponse encMessage = new EncMessageResponse();
        encMessage.setMessage(response.getMessage());
        encMessage.setSenderId(response.getSenderId());
        encMessage.setRecId(response.getRecId());
        encMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
       //send encryptedmessage
        SendEncMessage(encMessage);

        return encMessage;

    }

    public DecMessage getMessageDecrypted(String Message,String senderid,
                                 String peerid,String secretkey){

        String SecretKey = secretkey;
        initFromStrings(SecretKey, "e3IYYJC2hxe24/EO");
        String secretMessage = decrypt_symmetric(Message);
        DecMessage decMessage = new DecMessage();
        decMessage.setMessage(secretMessage);
        decMessage.setSenderId(senderid);
        decMessage.setRecId(peerid);

        return decMessage;

    }

    public DecMessage MessageDecrypted(String Message,String senderid,
                                          String peerid,String secretkey, Authentication authentication){

        String SecretKey = secretkey;
        WebClient webClient2 = webClientBuilder.build();
        WebClient webClient1= WebClient.create();

//        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8686/GetDecrypt")
//                .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
////                .queryParam("message",Message)
//                .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
//                .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
//                .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8))
//                .build()
//                .toUri();
//
//        DecMessage response = webClient2.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(DecMessage.class)
//                .block();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        DecMessage response = webClient2.get()
                .uri(builder -> {
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                            .scheme("http")
                            .host("DECSYM-SERVICE")
                            .path("/api/decsym/GetDecrypt")
                            .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
                            .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
                            .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
                            .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8));
                    return uriBuilder.build().toUri();
                })
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(DecMessage.class)
                .block();
        DecMessage decMessage = new DecMessage();
        decMessage.setMessage(response.getMessage());
        decMessage.setSenderId(senderid);
        decMessage.setRecId(peerid);

        return decMessage;

    }


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

    public PublicKeyMessage SendPublicKey_ca(Own_PublicKeyMessageSend publicKeyMessage)

    {
        PublicKeyMessage outMessage= new PublicKeyMessage();
        outMessage.setPublicKey(publicKeyMessage.getEnc_Sig_Own_Pubkey());
        outMessage.setSenderId(publicKeyMessage.getSenderId());
        outMessage.setRecId(publicKeyMessage.getRecId());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        messagingTemplate.convertAndSend("/topic/peer-public-key",outMessage);
        return outMessage;
    }

    public Peer_PublicKeyMessageSend SendPublicKey_ca_peer(Peer_PublicKeyMessageSend publicKeyMessage)

    {
        Peer_PublicKeyMessageSend outMessage= new Peer_PublicKeyMessageSend();
        outMessage.setDh_Pubkey(publicKeyMessage.getDh_Pubkey());
        outMessage.setCa_Pubkey(publicKeyMessage.getCa_Pubkey());
        outMessage.setSenderId(publicKeyMessage.getSenderId());
        outMessage.setRecId(publicKeyMessage.getRecId());
        messagingTemplate.convertAndSend("/topic/public_key_ca",outMessage);
        return outMessage;
    }

    public Peer_PublicKeyMessageSend SendPublicKey_ca_peer_rec(Peer_PublicKeyMessageSend publicKeyMessage)

    {
        Peer_PublicKeyMessageSend outMessage= new Peer_PublicKeyMessageSend();
        outMessage.setDh_Pubkey(publicKeyMessage.getDh_Pubkey());
        outMessage.setCa_Pubkey(publicKeyMessage.getCa_Pubkey());
        outMessage.setSenderId(publicKeyMessage.getSenderId());
        outMessage.setRecId(publicKeyMessage.getRecId());
        messagingTemplate.convertAndSend("/topic/public_key_ca_rec",outMessage);

        return outMessage;
    }

    public EncMessageResponse SendEncMessage(EncMessageResponse encMessageResponse)

    {
        EncMessageResponse outMessage= new EncMessageResponse();
        outMessage.setMessage(encMessageResponse.getMessage());
        outMessage.setSenderId(encMessageResponse.getSenderId());
        outMessage.setRecId(encMessageResponse.getRecId());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));

        messagingTemplate.convertAndSend("/topic/EncryptedMessage",outMessage);

        return outMessage;
    }


}
