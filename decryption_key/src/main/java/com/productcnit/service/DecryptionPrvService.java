package com.productcnit.service;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class DecryptionPrvService {


    private SecretKey key;
    private int KEY_SIZE = 128;
    private int T_LEN = 128;
    private byte[] IV;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    //private static final String PUBLIC_KEY_STRINGS="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";
    private static final String PUBLIC_KEY_STRINGS="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmE12WPyKfXoAuqHPWabgWbH5ZzEk2mlQWx/IgkFyqDjOKAlochyntt6V/cXo/H4HtIAOcrkCjkoZpnJD8fN8OkXLMD5R3AlK0/HZFaKBypcVz6gpZnM72PiJk/gWd5sBSmenGiK0q8tSCluV5+KN1zppO0BKIt91q9EFEJH9IeYRiADic+qTjgp6ENxKXNacvNNiTSopZFpMpBVy+IyNlYc84cOIhMhifLxEyrB8AcO7C3QnpgBsjaiBO11CEuDsrC33Fd6DTZeh+8CKdClGuVG6JejB+yOXhnvaHQnXbM4cvTf0Yz10doDe+rEyWugoi29iJBnkmrOiBCWcVh0MmQIDAQAB";
    private static final String PRIVATE_KEY_STRINGS= "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYTXZY/Ip9egC6oc9ZpuBZsflnMSTaaVBbH8iCQXKoOM4oCWhyHKe23pX9xej8fge0gA5yuQKOShmmckPx83w6RcswPlHcCUrT8dkVooHKlxXPqClmczvY+ImT+BZ3mwFKZ6caIrSry1IKW5Xn4o3XOmk7QEoi33Wr0QUQkf0h5hGIAOJz6pOOCnoQ3Epc1py802JNKilkWkykFXL4jI2Vhzzhw4iEyGJ8vETKsHwBw7sLdCemAGyNqIE7XUIS4OysLfcV3oNNl6H7wIp0KUa5Ubol6MH7I5eGe9odCddszhy9N/RjPXR2gN76sTJa6CiLb2IkGeSas6IEJZxWHQyZAgMBAAECggEAO9MZrMSuZbFZvfW3bCDXuYPmUrmEH2ME1HZ8nWW18ww1NpbTdxd4baWrRz7JUPz0a9ttUXUI4vmKb84skEqzPnSR5sZkA0mHIg+61Pu7feexApusf3i+RnECIaRuttalQCPNCMAfNegstrLZBHgCfUuNcF/Ff0NFhpMZKTQfOYP5s54r/rPcWAiHHQfQeVi3D6auIvdjmpFxfCJZ1d72/T4EpBQskcpcwBZ7MpgqLJosLRHJ1AyFXs0lWLC9bfHE+seieSOXy788jWwB1AAsQy7971d9LWf4hRhxYXM0+vHniN3CChzQfpdgF3Fgv4zioaf9XphPSJwD93Lex86FHQKBgQDIsC6oTziWBJFzoVTbNx5sCCA8xzSqsdBGcIMP0xQ5hnrVekZ4IqoZe5hwE9k/hkC2UFerW5yPKN5iAuhmiPN5F3eYIewRyR4d49vkrQ3DpjjJsGgPSqWQIZIn60lIbmAKqzKsMM2riaBCf13h0a1lhGdCaSoKf0YBx8eIt0tjewKBgQDCR16On2lGrfNFVCmiEj8SnsjoOqybOn1BSR+CYTLJx3VRrLa0ncF/gWlAEuIMVXDkVuQ1/CTIco62W6EPOSWUJ0mYksTrGtq1x3oM7nUomzwDHxTLqTYkJJewfCaVmHDZqvGIhN4fWTap6yLdAxetDgbFcFEjxsSk4pxJbtqZ+wKBgQCabjDJJYUWs9c1kn0Y33ZSjYI5ItWdUI1D1wqTn8sdZSWapqgZhBs+1sP74to3LtkXMgX0f9+erkVKSJCLbnFkyjEmaVweZ5P8Tj4E30ILPxrN4SiCM9/pZFvyyfSvfvf63bh1lmx9xr8CzL3yqoXy64sGKKTIRR4a+p/Jzbp+hwKBgDBFw3z0pxfkyPmE/ANFPV7HDWyGh7TaR50dKjMfK5tvb/snSw591EJ2SUyeZIRkBnT2EiZHzGvx6vvssIwzyt6RFHxQm5U/SvC369+isIGycKrl4ijolodMgFRdxMiO5BtxmiotuXNYm3iE8FiyGAPr5ErSqZaWIO5HUEX+wVpZAoGAVn8aI93ksxkDageRNm1rByCFjNHGqv/vMuB+hoQe3fUBZgOZPaY2XpO2WQysutQ4UGY7iV47jCEW9xfC0oI74OuWFJ+KFSmoPOwtAfhPmJj4b3K1DVg/R45mbdr8wgT1SYpRr0ZeGajKnVALxOQjy6k962LNOFXrlg+lP0+mSog=";


    //function for generating public key for signature
    public void initFromStringsPublickey()
    {
        try
        {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRINGS));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){}
    }
   // private static final String PRIVATE_KEY_STRINGS="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";


    public void initFromStrings()
    {
        try
        {
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRINGS));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        }
        catch (Exception ignored){}
    }

    public String decrypt(String encryptedMessage) throws Exception {

        byte[] encryptedBytes= decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
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
    public void initFromStrings_sym(String secretKey, String IV){
        key = new SecretKeySpec(decode(secretKey.toString()),"AES");
        this.IV = decode(IV);
    }

    public String decrypt_sym(String encryptedMessage) {
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



    private String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data)
    {
        return  Base64.getDecoder().decode(data);
    }


    public String getData()
    {
         initFromStrings();
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
            String secretMessage= decrypt(encryptedMessage);
//            System.err.println("The Secret Message\n"+secretMessage);
            return secretMessage;
        }
        catch (Exception ignored)
        {
            return null;
        }

    }

    public boolean getsig()
    {
        initFromStringsPublickey();
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


}
