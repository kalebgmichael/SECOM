package com.productcnit.controller;

import com.productcnit.dto.GenKeyPairResponse;
import com.productcnit.dto.SenRecResponse;
import com.productcnit.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequestMapping("/api/socket")
@CrossOrigin("*")
public class ViewController {

    @Autowired
    private final WebSocketService webSocketService;
    private final KafkaTemplate<String, SenRecResponse> kafkaloginTemplate;

    public ViewController(WebSocketService webSocketService, KafkaTemplate<String, SenRecResponse> kafkaloginTemplate) {
        this.webSocketService = webSocketService;
        this.kafkaloginTemplate = kafkaloginTemplate;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication)
    {
        try
        {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new SecurityException("Invalid authentication type");
        }
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Map<String, Object> claims = jwt.getClaims();
        System.out.println("claims"+claims);
        String userId =  authentication.getName();
        // Extract profile and email claims
        String email = (String) claims.get("email"); // Adjust claim key if needed
        String username = (String) claims.get("name"); // Adjust claim key if needed
        String ownerid = (String) claims.get("Owner_ID"); // Adjust claim key if needed
        System.out.println("ownerid"+ ownerid);
        System.out.println("email"+ email);
        System.out.println("userId"+ userId);
        SenRecResponse senRecResponse= new SenRecResponse(ownerid,userId);
        kafkaloginTemplate.send("Save-Gen-keypair-topic", "Gen-key-pair", senRecResponse);
        System.out.println("Successfully sent Gen-key-pair from user"+username);
        model.addAttribute("ownerId", ownerid);
        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
    }
        catch (Exception e) {
        //log.error("Error retrieving user information from JWT:", e);
    }

        return "example";
    }


}
