package com.productcnit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/socket")
@CrossOrigin("*")
public class ViewController {
    @GetMapping("/home")
    public String home()
    {
        return "example";
    }

}
