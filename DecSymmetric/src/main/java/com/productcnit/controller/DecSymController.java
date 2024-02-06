package com.productcnit.controller;

import com.productcnit.service.DecSymService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
