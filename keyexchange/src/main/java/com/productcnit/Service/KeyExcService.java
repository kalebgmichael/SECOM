package com.productcnit.Service;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyExcService {

    private static final int KEY_SIZE = 2048; // Adjust the key size as needed
    private static PublicKey ownpublicKey;

    private static PublicKey peerpublicKey;
    private static PublicKey peerpublicKeyfromparameter;
    private static PrivateKey privateKey;

    static String Peer_publickey="MIICKDCCARsGCSqGSIb3DQEDATCCAQwCggEBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7ORbPcIAfLihY78FmNpINhxV05ppFj+o/STPX4NlXSPco62WHGLzViCFUrue1SkHcJaWbWcMNU5KvJgE8XRsCMoYIXwykF5GLjbOO+OedywYDoYDmyeDouwHoo+1xV3wb0xSyd4ry/aVWBcYOZVJfOqVauUV0iYYmPoFEBVyjlqKrKpo//////////8CAQICAgDgA4IBBQACggEAQgb6IrVq4bn1+hOFH1+O5NswLdgFBb4PBsOF7Kh2mcbz5ibjeBQ0yrEw4qjL82/a1F+WVUzej15PXLicDjwoHgqKqma43qdZ4hbearyodKOHiSqpjOxpEejTAIYkOfe0FUw06ypgpi0+OVPY/JUXr0ZjHjuCoeniG8pNVOe3Bze1qZZ5uuAE9KLAPmeyPFYR+Y9QTOsrsMV8vNhIRXMXgo3QKBf/h+o5tvMjx5kxEeUxYEQCKnANyarxpAQBm9z/KJjl4gFFS2OVlNciOgcaSNPwnX4kGIpH2Djy8aUuGximg0cF5ldnxEGUNBvWuRtIdKSC8YkjJDVV3liQHsAaxw==";

    public  static void   generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ownpublicKey= keyPair.getPublic();
            privateKey=keyPair.getPrivate();


        } catch (Exception ignored) {
            System.out.println("Error in keypair generation");
        }
    }

    public static void initFromStringsPublickey()
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(Peer_publickey));
            peerpublicKey = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){}
    }

    public static void initFromPublickey(String Publickey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(Publickey));
            peerpublicKeyfromparameter = keyFactory.generatePublic(keySpecPublic);
        }
        catch (Exception ignored){}
    }

    public  String generatePublicKey()  {

        return encode(ownpublicKey.getEncoded());
    }

    public String generateSharedSecret() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
//            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decode(otherPublicKey));
//            PublicKey otherPublicKeyObject = keyFactory.generatePublic(x509KeySpec);

            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
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
        KeyExcService keyExcService = new KeyExcService();
        keyExcService.generateKeyPair();
        String Publickey= keyExcService.generatePublicKey();
        keyExcService.initFromStringsPublickey();
        System.out.println("the  public key is " + Publickey);
        String sharedkey= keyExcService.generateSharedSecret();
        System.out.println("the shared key is  "+sharedkey );
    }

}
