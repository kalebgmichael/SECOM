package com.productcnit.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Service
public class EncryptionPubService {

    private SecretKey key;
    private int KEY_SIZE = 128;
    private int T_LEN = 128;
    private byte[] IV;

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private PublicKey publicKey1;
    private PrivateKey privateKey1;
    private static final String PUBLIC_KEY_STRINGS11="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";
    private static final String PRIVATE_KEY_STRINGS11="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";


    private static final String PUBLIC_KEY_STRINGS="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmE12WPyKfXoAuqHPWabgWbH5ZzEk2mlQWx/IgkFyqDjOKAlochyntt6V/cXo/H4HtIAOcrkCjkoZpnJD8fN8OkXLMD5R3AlK0/HZFaKBypcVz6gpZnM72PiJk/gWd5sBSmenGiK0q8tSCluV5+KN1zppO0BKIt91q9EFEJH9IeYRiADic+qTjgp6ENxKXNacvNNiTSopZFpMpBVy+IyNlYc84cOIhMhifLxEyrB8AcO7C3QnpgBsjaiBO11CEuDsrC33Fd6DTZeh+8CKdClGuVG6JejB+yOXhnvaHQnXbM4cvTf0Yz10doDe+rEyWugoi29iJBnkmrOiBCWcVh0MmQIDAQAB";
    private static final String PRIVATE_KEY_STRINGS= "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYTXZY/Ip9egC6oc9ZpuBZsflnMSTaaVBbH8iCQXKoOM4oCWhyHKe23pX9xej8fge0gA5yuQKOShmmckPx83w6RcswPlHcCUrT8dkVooHKlxXPqClmczvY+ImT+BZ3mwFKZ6caIrSry1IKW5Xn4o3XOmk7QEoi33Wr0QUQkf0h5hGIAOJz6pOOCnoQ3Epc1py802JNKilkWkykFXL4jI2Vhzzhw4iEyGJ8vETKsHwBw7sLdCemAGyNqIE7XUIS4OysLfcV3oNNl6H7wIp0KUa5Ubol6MH7I5eGe9odCddszhy9N/RjPXR2gN76sTJa6CiLb2IkGeSas6IEJZxWHQyZAgMBAAECggEAO9MZrMSuZbFZvfW3bCDXuYPmUrmEH2ME1HZ8nWW18ww1NpbTdxd4baWrRz7JUPz0a9ttUXUI4vmKb84skEqzPnSR5sZkA0mHIg+61Pu7feexApusf3i+RnECIaRuttalQCPNCMAfNegstrLZBHgCfUuNcF/Ff0NFhpMZKTQfOYP5s54r/rPcWAiHHQfQeVi3D6auIvdjmpFxfCJZ1d72/T4EpBQskcpcwBZ7MpgqLJosLRHJ1AyFXs0lWLC9bfHE+seieSOXy788jWwB1AAsQy7971d9LWf4hRhxYXM0+vHniN3CChzQfpdgF3Fgv4zioaf9XphPSJwD93Lex86FHQKBgQDIsC6oTziWBJFzoVTbNx5sCCA8xzSqsdBGcIMP0xQ5hnrVekZ4IqoZe5hwE9k/hkC2UFerW5yPKN5iAuhmiPN5F3eYIewRyR4d49vkrQ3DpjjJsGgPSqWQIZIn60lIbmAKqzKsMM2riaBCf13h0a1lhGdCaSoKf0YBx8eIt0tjewKBgQDCR16On2lGrfNFVCmiEj8SnsjoOqybOn1BSR+CYTLJx3VRrLa0ncF/gWlAEuIMVXDkVuQ1/CTIco62W6EPOSWUJ0mYksTrGtq1x3oM7nUomzwDHxTLqTYkJJewfCaVmHDZqvGIhN4fWTap6yLdAxetDgbFcFEjxsSk4pxJbtqZ+wKBgQCabjDJJYUWs9c1kn0Y33ZSjYI5ItWdUI1D1wqTn8sdZSWapqgZhBs+1sP74to3LtkXMgX0f9+erkVKSJCLbnFkyjEmaVweZ5P8Tj4E30ILPxrN4SiCM9/pZFvyyfSvfvf63bh1lmx9xr8CzL3yqoXy64sGKKTIRR4a+p/Jzbp+hwKBgDBFw3z0pxfkyPmE/ANFPV7HDWyGh7TaR50dKjMfK5tvb/snSw591EJ2SUyeZIRkBnT2EiZHzGvx6vvssIwzyt6RFHxQm5U/SvC369+isIGycKrl4ijolodMgFRdxMiO5BtxmiotuXNYm3iE8FiyGAPr5ErSqZaWIO5HUEX+wVpZAoGAVn8aI93ksxkDageRNm1rByCFjNHGqv/vMuB+hoQe3fUBZgOZPaY2XpO2WQysutQ4UGY7iV47jCEW9xfC0oI74OuWFJ+KFSmoPOwtAfhPmJj4b3K1DVg/R45mbdr8wgT1SYpRr0ZeGajKnVALxOQjy6k962LNOFXrlg+lP0+mSog=";




    // generate private key function
    public void initFromStringsprivate()
    {
        try
        {
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRINGS));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        }
        catch (Exception ignored){}
    }
    public void initFromStringsprivate_peer(String privatekey)
    {
        try
        {
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(privatekey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            privateKey1 = keyFactory.generatePrivate(keySpecPrivate);
        }
        catch (Exception ignored){}
    }
    public void initFromStrings()
    {
        try
        {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRINGS));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){}
    }
    public void initFromStrings_peer(String publickey)
    {
        try
        {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(publickey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey1 = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){}
    }


    public String encrypt(String message) throws Exception
    {
        byte[] messageTobytes= message.getBytes();
        Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptedbytes= cipher.doFinal(messageTobytes);
        return encode(encryptedbytes);

    }

    public String encrypt_peer(String message) throws Exception
    {
        byte[] messageTobytes= message.getBytes();
        Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey1);
        byte[] encryptedbytes= cipher.doFinal(messageTobytes);
        return encode(encryptedbytes);

    }

    // fucntion for generating a signature with private key
    public String generateSignature(String message) throws Exception{
        Signature signature= Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        byte[] bytes=message.getBytes();
        signature.update(bytes);
        byte[] finalSignature=signature.sign();
        return encode(finalSignature);
    }

    public String generateSignature_peer(String message) throws Exception{
        Signature signature= Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        byte[] bytes=message.getBytes();
        signature.update(bytes);
        byte[] finalSignature=signature.sign();
        return encode(finalSignature);
    }


    public SecretKey initFromStrings(String IV) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
        this.IV = decode(IV);
        return key;
    }

    public String encrypt_sym(String message) {
        try
        {
            byte[] messageInBytes = message.getBytes();
            Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
            return encode(encryptedBytes);
        }
        catch (Exception ignored)
        {
            System.out.println("error in encrypt");
            return null;
        }


    }

    public String convertSignature(byte[] signature){//convert signature
        return new String(encode(signature));
    }

    public String convertPublicKey(PublicKey publicKey){//convert publicKey
        return new String(encode(publicKey.getEncoded()));
    }


    public String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] decode(String data)
    {
        return  Base64.getDecoder().decode(data);
    }

    // Method to compress data using zlib
    public static byte[] Compress(byte[] data) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream);
            deflaterOutputStream.write(data);
            deflaterOutputStream.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to decompress data using zlib
    public static byte[] Decompress(byte[] compressedData) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
            InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inflaterInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
