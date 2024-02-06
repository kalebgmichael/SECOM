package com.productcnit.controller;


import com.productcnit.service.DecryptionPrvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecryptionPrvController {

   private final DecryptionPrvService decryptionPrvService;
    @GetMapping("/getmsg")
    public String getdata()
    {
        return decryptionPrvService.getData();
    }

    @GetMapping("/getsig")
    public boolean getsign()
    {
        return decryptionPrvService.getsig();
    }
}
