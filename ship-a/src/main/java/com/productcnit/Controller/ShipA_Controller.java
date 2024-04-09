package com.productcnit.Controller;


import com.productcnit.Service.ShipA_Service;
import com.productcnit.dto.ShipRouting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/ship")
@RequiredArgsConstructor
public class ShipA_Controller {

    @Autowired
    private final ShipA_Service shipAService;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/resource")
    public String getResource() {
        return "This is a secured resource";
    }


    @GetMapping("/sendrouting")
    public ResponseEntity<String> sendrouting(@RequestParam("Recid") String Recid, @RequestParam("SenderId") String SenderId, @RequestParam("publicKey") String publicKey, Authentication authentication)
    {
        String secretMessage = shipAService.SendRouting(Recid,SenderId,publicKey,authentication);
        if (secretMessage != null) {
            return ResponseEntity.ok(secretMessage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data");
        }


    }
    @PostMapping("/route")
    public ResponseEntity<String> createShipRouting(@RequestBody ShipRouting shipRouting,Authentication authentication) {
        // Use the service to process the ShipRouting object
        String response=shipAService.processShipRouting(shipRouting,authentication);
        return ResponseEntity.ok(response);
    }

}


