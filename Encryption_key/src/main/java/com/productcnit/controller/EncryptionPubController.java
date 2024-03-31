package com.productcnit.controller;



import com.productcnit.service.EncryptionPubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.awt.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class EncryptionPubController {


    private final EncryptionPubService encryptionPubService;

    @GetMapping("getsecret")
    public String getmessage()
    {
        String message= "kaleb";

        encryptionPubService.initFromStrings();
        encryptionPubService.initFromStringsprivate();
        try
        {
            return encryptionPubService.encrypt(message);
        }
        catch (Exception ignored){
            return null;
        }
    }

    @GetMapping("getencrypt")
    public String getencrypt(@RequestParam("message") String message)
    {
        String message1 = URLDecoder.decode(message, StandardCharsets.UTF_8);
        encryptionPubService.initFromStringsprivate();
        encryptionPubService.initFromStrings();
        try
        {

            String encryptedMessage= encryptionPubService.encrypt(message1);
            //String encodedMessage = URLEncoder.encode(encryptedMessage, StandardCharsets.UTF_8);
            return encryptedMessage;
        }
        catch (Exception ignored){
            return null;
        }
    }

    @GetMapping("getenc_sig")
    public String getenc_sig(@RequestParam("message") String message)
    {
        String message1 = URLDecoder.decode(message, StandardCharsets.UTF_8);
        encryptionPubService.initFromStringsprivate();
        encryptionPubService.initFromStrings();
        try
        {

            String encryptedMessage= encryptionPubService.encrypt(message1);
            String signature_key=  encryptionPubService.generateSignature(encryptedMessage);
            String enc_sign= encryptedMessage+"_.._"+ signature_key;
            //String encodedMessage = URLEncoder.encode(encryptedMessage, StandardCharsets.UTF_8);
            return enc_sign;
        }
        catch (Exception ignored){
            return null;
        }
    }

    @GetMapping("getenc_sig_peer")
    public String getenc_sig_peer(@RequestParam("message") String message, @RequestParam("publickey_peer") String publickey_peer) throws Exception {
        String message1 = URLDecoder.decode(message, StandardCharsets.UTF_8);
        String publickey_peer1 = URLDecoder.decode(publickey_peer, StandardCharsets.UTF_8);
        System.out.println("this is the public key"+publickey_peer1);
        System.out.println("this is the public key size"+publickey_peer1.length());
        encryptionPubService.initFromStrings_peer(publickey_peer1);
        SecretKey key= encryptionPubService.initFromStrings("e3IYYJC2hxe24/EO");
        String sym_key= encryptionPubService.encode(key.getEncoded());
        System.out.println("this is the sym_key"+sym_key);
        String encryptedPubKey= encryptionPubService.encrypt_sym(message1);
        System.out.println("the encryptedPubKey to be sent"+ encryptedPubKey);
        System.out.println("the encryptedPubKey to be sent length"+ message.length());
//        try
//        {
            String encryptedMessage= encryptionPubService.encrypt_peer(sym_key);
            System.out.println("encrypted message by public key"+encryptedMessage);
            encryptionPubService.initFromStringsprivate();
            String signature_key=  encryptionPubService.generateSignature_peer(encryptedMessage);
            String enc_sign= encryptedMessage+"_.._"+ signature_key+"_.._"+encryptedPubKey;
            //String encodedMessage = URLEncoder.encode(encryptedMessage, StandardCharsets.UTF_8);
            return enc_sign;
//        }
//        catch (Exception ignored){
//            System.out.println("error in getenc_sig_peer ");
//            return null;
//        }
    }

    @GetMapping("getsig")
    public String getSig()
    {
        String message1= "kaleb";
        String x="x";
        encryptionPubService.initFromStringsprivate();
        try
        {
            return encryptionPubService.generateSignature(message1);
        }
        catch (Exception ignored){
            return null;
        }
    }

    @GetMapping("getsig_key")
    public String getSig_key(@RequestParam("message") String message)
    {
        String message1 = URLDecoder.decode(message, StandardCharsets.UTF_8);
        encryptionPubService.initFromStringsprivate();
        try
        {
            return encryptionPubService.generateSignature(message1);
        }
        catch (Exception ignored){
            return null;
        }
    }

    @GetMapping("getsigVerify")
    public String getsigVerify(@RequestParam("message") String message)
    {

        encryptionPubService.initFromStringsprivate();
        try
        {
            return encryptionPubService.generateSignature(message);
        }
        catch (Exception ignored){
            return null;
        }
    }
}
