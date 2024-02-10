package com.productcnit.Service;

public class KeyClientone {
    public static void main(String[] args) {
        KeyManager keyManager = new KeyManager();
        keyManager.generateKeyPair();
        String publicKey = keyManager.generatePublicKey();
        String privatekey= keyManager.generatePrviatekey();
        System.out.println("Client 1 public key: " + publicKey);
        System.out.println("Client 1 privatekey: " + privatekey);
        String Publickeypeer="MIICKDCCARsGCSqGSIb3DQEDATCCAQwCggEBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7ORbPcIAfLihY78FmNpINhxV05ppFj+o/STPX4NlXSPco62WHGLzViCFUrue1SkHcJaWbWcMNU5KvJgE8XRsCMoYIXwykF5GLjbOO+OedywYDoYDmyeDouwHoo+1xV3wb0xSyd4ry/aVWBcYOZVJfOqVauUV0iYYmPoFEBVyjlqKrKpo//////////8CAQICAgDgA4IBBQACggEAFDtp7TzFue4NCjhRf9rrFaq4hRrc4F5196Z8BVLaggVD6jqhc+42kbgdIgX1czq6Gk9SXAYjxknH7amlTvpbz+yiI2fsobERbRA7tlX0wzpThXi8DMw+pd0LLSwZii6gElWKIQHNrcwTtKG91FkJzYZOS9pAHFwB10YvZKxaK47eDwwkcBtNhaXopeeCSZKuZOTAMZxMQ+mjzuzv5WIB+1RQbGSrChTAduNjw9fOrx4vrDjv8EyjkXQmj03ZKgzkS3QiA+msaCUl3juW6Xl+BK+Etesi1lI/QFTs4V4Ty7XKGD++ex7dInjMjarfxBliuu5CV/Exhhro51M82pjjXw==";
        String Privatekeypeer="MIIBQwIBADCCARsGCSqGSIb3DQEDATCCAQwCggEBAP//////////yQ/aoiFowjTExmKLgNwc0SkCTgiKZ8x0Agu+pjsTmyJRSgh5jjQE3e+VGbPNOkMbMCsKbfJfFDdP4TVtbVHCReSFtXZiXn7G9ExC6aY37WsL/1y29Aa37e44a/taiZ+lrp8kEXxLH+ZJKGZR7ORbPcIAfLihY78FmNpINhxV05ppFj+o/STPX4NlXSPco62WHGLzViCFUrue1SkHcJaWbWcMNU5KvJgE8XRsCMoYIXwykF5GLjbOO+OedywYDoYDmyeDouwHoo+1xV3wb0xSyd4ry/aVWBcYOZVJfOqVauUV0iYYmPoFEBVyjlqKrKpo//////////8CAQICAgDgBB8CHQDiV3EAkoDmaynOkrCez8Q+ErC7wGjP6vcV4t8i";

        // Save publicKey and send to the other client
//        KeyManager keyManager2= new KeyManager();
//        keyManager2.generateKeyPair();
//        keyManager2.initFromStringsPublickey(Publickeypeer);
//        keyManager2.initFromStringsPrvkey(Privatekeypeer);
//        String sharedKey = keyManager2.generateSharedSecret();
//        System.out.println("Shared key: " + sharedKey);
    }
}
