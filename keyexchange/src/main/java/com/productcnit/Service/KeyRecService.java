package com.productcnit.Service;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyRecService {
    private static final int KEY_SIZE = 2048; // Adjust the key size as needed
    private static PublicKey ownpublicKey;
    private static PublicKey peerpublicKey;
    private static PublicKey peerpublicKeyfromparameter;
    private static PrivateKey privateKey;

    static String Public_key_peer ="MIICKTCCARsGCSqGSIb3DQEDATCCAQwCggEBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7ORbPcIAfLihY78FmNpINhxV05ppFj+o/STPX4NlXSPco62WHGLzViCFUrue1SkHcJaWbWcMNU5KvJgE8XRsCMoYIXwykF5GLjbOO+OedywYDoYDmyeDouwHoo+1xV3wb0xSyd4ry/aVWBcYOZVJfOqVauUV0iYYmPoFEBVyjlqKrKpo//////////8CAQICAgDgA4IBBgACggEBAMSOOkKTRbwfJHf3NaFq0ZUEcaRu1/vjr4KHQTeZvPX93Vn6NJHDPytgeHtJ6eUz8sSxmJH89UiQFO174YIeBlhw8oYdALrr54nnTu80VCHgvBu6pHnkBc5IydD1bg/kqBjnSYWfrtHzxFUFf+n4C7EyenibbjztxlOQZv8GTweMk7nx9BTIm7NhoBZWJZyLQBGl+eFezoum7XsgKaMMGC03usIA5v4eOMThHRNr/PYxwqE2AVxKzk2ERENFtE2wvUyyyOu5CNrPnTzHEIZMB9QvE23eQU9oZtCDGPuMBqVtHmLnv/e6j2KiukLET4aznLEln2/cjg+V5KyVCaRLaB8=";


    public static void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey=keyPair.getPrivate();
            ownpublicKey=keyPair.getPublic();


        } catch (Exception ignored) {
            System.out.println("Error in keypair generation");
        }
    }

    public  String generatePublicKey()  {

        return encode(ownpublicKey.getEncoded());
    }

    public static void initFromStringsPublickey()
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(Public_key_peer));
            peerpublicKey = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){}
    }

    public static void initFromPublickey(String publickey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(publickey));
            peerpublicKeyfromparameter = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){}
    }



    public String generateSharedSecret(PrivateKey ownprivateKey) {
        try {

            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(ownprivateKey);
            keyAgreement.doPhase(peerpublicKey, true);

            // Use SecretKeySpec directly
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyAgreement.generateSecret(), 0, 16, "AES");

            return encode(secretKeySpec.getEncoded());
        } catch (Exception e) {
            System.out.println("Error in shared key generation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    private static String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] decode(String data)
    {
        return  Base64.getDecoder().decode(data);
    }



    public static void main(String[] args) {
        KeyRecService keyRecService = new KeyRecService();
        keyRecService.generateKeyPair();
        String Publickey= keyRecService.generatePublicKey();
        System.out.println("the  public key is " + Publickey);
        keyRecService.initFromStringsPublickey();
        String sharedkey= keyRecService.generateSharedSecret(privateKey);
        System.out.println("the shared key is  "+sharedkey );
    }
}
