package com.productcnit.Service;

import java.beans.PropertyVetoException;
import java.security.PrivateKey;

public class KeyClienttwo {

    public static void main(String[] args) {
        KeyManager keyManager = new KeyManager();
        keyManager.generateKeyPair();
        String publicKey = keyManager.generatePublicKey();
        System.out.println("Client 2 public key: " + publicKey);
        String privatekey= keyManager.generatePrviatekey();
        System.out.println("Client 2 privatekey: " + privatekey);



    }
}
