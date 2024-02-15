package com.productcnit.Controller;


import com.productcnit.Service.ShipA_Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/ship")
public class ShipA_Controller {
    @GetMapping("/resource")
    public String getResource() {
        return "This is a secured resource";
    }

}
