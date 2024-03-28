package com.productcnit.controller;

import com.productcnit.dto.DecMessage;
import com.productcnit.dto.EncMessage;
import com.productcnit.service.DecSymService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class DecSymController {

    private final DecSymService decSymService;

    @GetMapping("getmsg")
    public String getdata()
    {
        return  decSymService.getData();
    }

    @GetMapping("getDecrypt")
    public String getDecrypt()
    {
        return  decSymService.getData();
    }

    @GetMapping("/getDecryptmessage")
    public DecMessage getDecrypt(@RequestParam("encryptedMessage") String encryptedMessage,@RequestParam("senderid") String senderid,
                                 @RequestParam("secretkey") String secretkey, @RequestParam("peerid") String peerid)

    {
        System.out.println("this is secretkey"+ secretkey );
//        String SecretKey = secretkey.replaceAll("^\"|\"$", ""); // Remove leading and trailing quotation marks
        String SecretKey = secretkey;
        System.out.println("this is secretkey"+ SecretKey );
        return  decSymService.getDecrypt(encryptedMessage,senderid,peerid,secretkey);
    }

    @GetMapping("/Decrypt")
    public DecMessage Decrypt(@RequestParam("encryptedMessage") String encryptedMessage,@RequestParam("senderid") String senderid,
                              @RequestParam("peerid") String peerid,@RequestParam("secrectkey") String secrectkey)
    {
        decSymService.initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==", "e3IYYJC2hxe24/EO");

        String decryptedmessage= decSymService.decrypt(encryptedMessage);
        DecMessage decMessage= new DecMessage();
        decMessage.setMessage(decryptedmessage);
        decMessage.setSenderId(senderid);
        decMessage.setRecId(peerid);
        return decMessage;
    }
    @GetMapping("/GetDecrypt")
    public DecMessage GetDncrypt(@RequestParam("message") String message, @RequestParam("secretkey") String secretkey,
                              @RequestParam("sendid") String sendid, @RequestParam("peerid") String peerid)
    {
        String message1 = URLDecoder.decode(message, StandardCharsets.UTF_8);
//        String message1 = message;
        String secretkey1 = URLDecoder.decode(secretkey, StandardCharsets.UTF_8);
        String sendid1 = URLDecoder.decode(sendid, StandardCharsets.UTF_8);
        String peerid1 = URLDecoder.decode(peerid, StandardCharsets.UTF_8);

        System.out.println("secretkey is " + secretkey1);
        System.out.println("message1 in encryptsym is " + message1);
        System.out.println("message in encryptsym is " + message);
//        String message2 = message1.replaceAll(
//        "|\"$", "");
        String SecretKey = secretkey1;
        decSymService.initFromStrings(SecretKey, "e3IYYJC2hxe24/EO");
        String decryptedmessage= decSymService.decrypt(message1);
        System.out.println("dectryptedmessage in GetDncrypt is " + decryptedmessage);
        DecMessage decMessage = new DecMessage();
        decMessage.setMessage(decryptedmessage);
        decMessage.setSenderId(sendid1);
        decMessage.setRecId(peerid1);
        return decMessage;
    }
}
