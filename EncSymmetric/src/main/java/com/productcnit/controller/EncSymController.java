package com.productcnit.controller;


import com.productcnit.dto.EncMessage;
import com.productcnit.service.EncSymService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class EncSymController {

    private final EncSymService encSymService;

    @GetMapping("/getsecret")
    public String getdata()
    {
        encSymService.initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==", "e3IYYJC2hxe24/EO");

        return encSymService.encrypt("kalebCyber");
    }

    @GetMapping("/Encrypt")
    public EncMessage Encrypt(@RequestParam("message") String message,@RequestParam("secretkey") String secretkey,
                              @RequestParam("sendid") String sendid,@RequestParam("peerid") String peerid)
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
        encSymService.initFromStrings(SecretKey, "e3IYYJC2hxe24/EO");
        String encryptedmessage= encSymService.encrypt(message1);
        System.out.println("encryptedmessage in encryptsym is " + encryptedmessage);
        EncMessage encMessage = new EncMessage();
        encMessage.setMessage(encryptedmessage);
        encMessage.setSenderId(sendid);
        encMessage.setRecId(peerid);
        return encMessage;
    }
}
