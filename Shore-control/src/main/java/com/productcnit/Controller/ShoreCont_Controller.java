package com.productcnit.Controller;


import com.productcnit.Service.ShoreCont_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/client")
public class ShoreCont_Controller {


   private final ShoreCont_Service shoreCont_service;

    public ShoreCont_Controller(ShoreCont_Service shoreContService) {
        shoreCont_service = shoreContService;
    }

    @GetMapping("/initiateKeyExchange")
    public String initiateKeyExchange() throws NoSuchAlgorithmException {
        // Delegate to the service to initiate the key exchange
        return shoreCont_service.initiateKeyExchange();
    }

    @GetMapping("/completeKeyExchange")
    public String completeKeyExchange(String serverPublicKey) throws Exception {
        // Delegate to the service to complete the key exchange
        return shoreCont_service.completeKeyExchange(serverPublicKey);
    }

}
