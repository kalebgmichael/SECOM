package com.productcnit.Service;

import com.productcnit.repository.KeyPairRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class KeyManager {
    private static final int KEY_SIZE = 2048;
    private static PublicKey ownpublicKey;
    private static PrivateKey privateKey;
    private  PrivateKey privateKeyfromStr;
    private  PublicKey peerpublicKey;

    @Autowired
   private KeyPairRespository keyPairRespository;
    public static void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ownpublicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (Exception ignored) {
            System.out.println("Error in keypair generation");
        }
    }

    public  void initFromStringsPublickey(String publicKeyString) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(publicKeyString));
            peerpublicKey = keyFactory.generatePublic(keySpecPublic);
        } catch (Exception ignored) {
        }
    }

    public void initFromStringsPrvkey(String prvKeyString) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(prvKeyString));
            privateKeyfromStr = keyFactory.generatePrivate(keySpecPrivate);
        } catch (Exception ignored) {
        }
    }

    public String generatePublicKey() {
        return encode(ownpublicKey.getEncoded());
    }
    public String generatePrviatekey()
    {
        return encode(privateKey.getEncoded());
    }

    public String generateSharedSecret() {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKeyfromStr);
            keyAgreement.doPhase(peerpublicKey, true);

            SecretKeySpec secretKeySpec = new SecretKeySpec(keyAgreement.generateSecret(), 0, 16, "AES");

            return encode(secretKeySpec.getEncoded());
        } catch (Exception e) {
            System.out.println("Error in shared key generation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
