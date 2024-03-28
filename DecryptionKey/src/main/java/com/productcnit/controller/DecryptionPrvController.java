package com.productcnit.controller;


import com.productcnit.service.DecryptionPrvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.ServerSocket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class DecryptionPrvController {

   private final DecryptionPrvService decryptionPrvService;
    @GetMapping("/getmsg")
    public String getdata()
    {
        return decryptionPrvService.getData();
    }

    @GetMapping("/getmessage")
    public String getmessage(@RequestParam("encryptedmessage") String encryptedmessage) throws Exception {
        String message1 = URLDecoder.decode(encryptedmessage, StandardCharsets.UTF_8);
        System.out.println("encryptedmessage"+encryptedmessage);
        System.out.println("message1"+message1);
        decryptionPrvService.initFromStringsPublickey();
        decryptionPrvService.initFromStrings();
        return decryptionPrvService.decrypt(message1);
    }

    @GetMapping("/get_enc_sig_verif")
    public String get_enc_sig_verif(@RequestParam("encryptedmessage") String encryptedmessage) throws Exception {
        String message1 = URLDecoder.decode(encryptedmessage, StandardCharsets.UTF_8);
        System.out.println("encryptedmessage"+encryptedmessage);
        System.out.println("message1"+message1);
        String[] data= message1.split("_.._");
        String enc_mess= data[0];
        String enc_sign= data[1];
        decryptionPrvService.initFromStringsPublickey();
        decryptionPrvService.initFromStrings();
        boolean response = decryptionPrvService.verifySignature(enc_mess,enc_sign);
        System.out.println("the response is "+response);
        if(response)
        {
            decryptionPrvService.initFromStringsPublickey();
            decryptionPrvService.initFromStrings();
           String decmessage= decryptionPrvService.decrypt(enc_mess);
           return decmessage;
        }
        else
        {
            System.out.println("error in signature");
            decryptionPrvService.initFromStringsPublickey();
            decryptionPrvService.initFromStrings();
            return decryptionPrvService.decrypt(enc_mess);
        }


    }

    @GetMapping("/getsig")
    public boolean getsign()
    {
        return decryptionPrvService.getsig();
    }

    @GetMapping("/getsigVerify")
    public boolean getsigVerify(@RequestParam("encryptedmessage") String encryptedmessage,@RequestParam("signature")  String signature) throws Exception {
        return decryptionPrvService.verifySignature(encryptedmessage,signature);
    }
    @GetMapping("/getsigVerify_enckey")
    public boolean getsigVerify_enckey(@RequestParam("encryptedmessage") String encryptedmessage,@RequestParam("signature")  String signature) throws Exception {
        String message1 = URLDecoder.decode(encryptedmessage, StandardCharsets.UTF_8);
        String signature1 = URLDecoder.decode(signature, StandardCharsets.UTF_8);
        return decryptionPrvService.verifySignature(message1,signature1);
    }
}
