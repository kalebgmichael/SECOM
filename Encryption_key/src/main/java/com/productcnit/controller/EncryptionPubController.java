package com.productcnit.controller;



import com.productcnit.service.EncryptionPubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
