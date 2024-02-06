package com.productcnit.Controller;


import com.productcnit.Service.ShipA_Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/Server")
public class ShipA_Controller {

    private final ShipA_Service shipA_service;
    private final String shore = "http://localhost:8086/client";

    public ShipA_Controller(ShipA_Service shipAService) {
        shipA_service = shipAService;
    }


    @GetMapping("/initiateKeyExchange")
    public String initiateKeyExchange() throws NoSuchAlgorithmException {
        // Delegate to the service to initiate the key exchange
        return shipA_service.initiateKeyExchange();
    }



    @GetMapping("/completeKeyExchange")
    public String completeKeyExchange() throws Exception {
        // Delegate to the service to complete the key exchange
        String clientPublicKey = new RestTemplate().getForObject(shore + "/initiateKeyExchange", String.class);
        System.out.println( clientPublicKey);
        return shipA_service.completeKeyExchange(clientPublicKey);
    }
}
