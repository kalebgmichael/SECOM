package com.productcnit;

import javax.crypto.KeyAgreement;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;

public class DH {
    public static void main(String[] args) throws Exception {
        // Alice
        KeyPairGenerator aliceKpg = KeyPairGenerator.getInstance("DiffieHellman");
        aliceKpg.initialize(2048);
        KeyPair aliceKp = aliceKpg.generateKeyPair();

        // Bob
        KeyPairGenerator bobKpg = KeyPairGenerator.getInstance("DiffieHellman");
        bobKpg.initialize(2048);
        KeyPair bobKp = bobKpg.generateKeyPair();

        // Alice computes secret
        KeyAgreement aliceKeyAgreement = KeyAgreement.getInstance("DiffieHellman");
        aliceKeyAgreement.init(aliceKp.getPrivate());
        aliceKeyAgreement.doPhase(bobKp.getPublic(), true);
        byte[] aliceSecret = aliceKeyAgreement.generateSecret();

        // Bob computes secret
        KeyAgreement bobKeyAgreement = KeyAgreement.getInstance("DiffieHellman");
        bobKeyAgreement.init(bobKp.getPrivate());
        bobKeyAgreement.doPhase(aliceKp.getPublic(), true);
        byte[] bobSecret = bobKeyAgreement.generateSecret();

        // To verify both have generated the same secret
        System.out.println("Shared secret match: " + Arrays.equals(aliceSecret, bobSecret));

        // The value of shared secret of Alice
        System.out.println("Alice's shared secret: " + byteArrayToHexString(aliceSecret));

        // The value of shared secret of Bob
        System.out.println("Bob's shared secret: " + byteArrayToHexString(bobSecret));
    }

    private static String byteArrayToHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
