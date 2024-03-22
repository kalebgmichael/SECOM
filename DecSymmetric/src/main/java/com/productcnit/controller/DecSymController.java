package com.productcnit.controller;

import com.productcnit.dto.DecMessage;
import com.productcnit.service.DecSymService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
