package com.productcnit.controller;


import com.productcnit.service.EncSymService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
