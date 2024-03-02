package com.productcnit.controller;

import com.productcnit.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
@RequestMapping("/api/socket")
@CrossOrigin("*")
public class ViewController {

    @Autowired
    private final WebSocketService webSocketService;

    public ViewController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication)
    {    try {
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw new SecurityException("Invalid authentication type");
        }

        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Map<String, Object> claims = jwt.getClaims();


        String userId =  (String) claims.get("name");
        // Extract profile and email claims
        String email = (String) claims.get("email"); // Adjust claim key if needed
        String firstName = (String) claims.get("firstName"); // Adjust claim key if needed
        String lastName = (String) claims.get("lastName"); // Adjust claim key if needed

        // Combine for a username-like identifier (optional)
        String username = String.format("%s %s", firstName, lastName);
        String ownerid = (String) claims.get("Owner_ID"); // Adjust claim key if needed
        System.out.println("ownerid"+ ownerid);
        System.out.println("email"+ email);
        model.addAttribute("ownerId", ownerid);
        model.addAttribute("userId", userId);

    } catch (Exception e) {
        //log.error("Error retrieving user information from JWT:", e);
    }

        return "example";
    }

    @GetMapping("/data")
    public ResponseEntity<String> getData(@RequestParam("privatekey") String privateKey) {
        String secretMessage = webSocketService.getData(privateKey);
        if (secretMessage != null) {
            return ResponseEntity.ok(secretMessage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data");
        }
    }



}
