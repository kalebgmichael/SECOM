package com.productcnit.Controller;


import com.nimbusds.jose.shaded.gson.Gson;
import com.productcnit.Service.ShoreCont_Service;
import com.productcnit.dto.GenKeyPairResponse;
import com.productcnit.dto.KeyPairResponse;
import com.productcnit.dto.SenRecResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Shore")
@CrossOrigin("*")
public class ShoreCont_Controller {

    private final WebClient webClient = WebClient.builder().build();

    private final KafkaTemplate<String, SenRecResponse> kafkaloginTemplate;

    public ShoreCont_Controller(KafkaTemplate<String, SenRecResponse> kafkaloginTemplate) {
        this.kafkaloginTemplate = kafkaloginTemplate;
    }


    @GetMapping("/home")
    public String home()
    {
        return "example";
    }

//    @GetMapping("/home1")
//    public String home1()
//    {
//        String response="home1";
//        return response;
//    }
    @GetMapping("/home2")
    public ResponseEntity<String> home2() {
        String response = "hello";
        return ResponseEntity.ok(response);
    }
    @GetMapping("/login")
    public ResponseEntity<String> login(Principal principal) {

        String userInfo = getUserId(principal);
        String userId = userName(SecurityContextHolder.getContext().getAuthentication());
        String ownerID=Owner_ID(SecurityContextHolder.getContext().getAuthentication());

        System.out.println("this is userinfo "+principal.getName());
        System.out.println("this is OwnerID "+ownerID);


        SenRecResponse senRecResponse= new SenRecResponse(ownerID,userId);
        kafkaloginTemplate.send("Save-Gen-keypair-topic", "Gen-key-pair", senRecResponse);
            return ResponseEntity.ok("OwnerId sent  successfully");
    }

    @GetMapping("/userName")
    @KafkaListener(topics = "Username-topic", groupId = "group-id3")
    public String userName(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String userId = oauth2User.getAttribute("preferred_username");
            if (userId != null) {
                return "User ID: " + userId;
            } else {
                return "User ID not available";
            }
        } else {
            return "User ID not available";
        }
    }
    @GetMapping("/userId")
    private String getUserId(Principal authentication) {

       System.out.println(authentication);
        return "not null";
    }
//    @GetMapping("/userId1")
//    private String getUserId1(Authentication authentication) {
//
//        System.out.println(authentication);
//        return "not null";
//    }

    @GetMapping("/userId2")
    private ResponseEntity<Map<String, String>> getUserId2(Authentication authentication) {
        Map<String, String> response = new HashMap<>();

        try {
            if (!(authentication instanceof JwtAuthenticationToken)) {
                throw new SecurityException("Invalid authentication type");
            }

            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            Map<String, Object> claims = jwt.getClaims();

            // Extract profile and email claims
            String email = (String) claims.get("email"); // Adjust claim key if needed
            String firstName = (String) claims.get("firstName"); // Adjust claim key if needed
            String lastName = (String) claims.get("lastName"); // Adjust claim key if needed

            // Combine for a username-like identifier (optional)
            String username = String.format("%s %s", firstName, lastName);
            String ownerid = (String) claims.get("Owner_ID"); // Adjust claim key if needed
            System.out.println("ownerid"+ ownerid);

            response.put("email", email);
            response.put("username", username); // You can add more profile details as needed

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            //log.error("Error retrieving user information from JWT:", e);
            response.put("error", "Failed to retrieve user information");
            return ResponseEntity.badRequest().body(response);
        }
    }

//    public String getuserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser) {
//            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
//            String userId = oidcUser.getName(); // Or any other attribute that contains the user ID
//            return "User ID: " + userId;
//        } else {
//            return "User not authenticated";
//        }
//    }

    @GetMapping("/OwnerID")
    @KafkaListener(topics = "OwnerID-topic", groupId = "group-id3")
    public String Owner_ID(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String ownerId = (String) oauth2User.getAttribute("Owner_ID");
            if (ownerId != null) {
                return "Owner ID: " + ownerId;

            } else {
                return "Owner ID not available";
            }
        } else {
            return "User ID not available";
        }
    }



    //    @GetMapping("/sendpub")
//    public String Sendpub()
//    {
//        Jwt jwt =(Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        String response= webClient.get()
//                .uri("http://localhost:8089/key/sendpub")
//                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        return "This is message form keyexhcnage  "+ response;
//    }
    @GetMapping("/sendpub1")
    public String Sendpub1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            String tokenValue = oidcUser.getIdToken().getTokenValue();

            String response = webClient.get()
                    .uri("http://KEYEX-SERVICE/api/key/sendpub")
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return "This is message from keyexchange " + response;
        } else {
            return "User not authenticated";
        }
    }

    @GetMapping("/sendpub2")
    public String Sendpub2(Principal principal) {
        String userInfo = getUserId(principal);
        String userId = userName(SecurityContextHolder.getContext().getAuthentication());
        String ownerID=Owner_ID(SecurityContextHolder.getContext().getAuthentication());

        System.out.println("this is userinfo "+userInfo);
        System.out.println("this is userid "+userId);
        System.out.println("this is OwnerID "+ownerID);

        if (!"User not authenticated".equals(userInfo) && !"User not available".equals(userId)) {
            // User is authenticated, proceed with sending the public key
            DefaultOidcUser oidcUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String tokenValue = oidcUser.getIdToken().getTokenValue();

            String response = webClient.get()
                    .uri("http://KEYEX-SERVICE/api/key/sendpub")
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return "This is message from keyexchange " + response;
        } else {
            // User not authenticated or user ID not available
            return "User not authenticated or user ID not available";
        }
    }

    @GetMapping("/callGengetkeypair/{Id}")
    public ResponseEntity<GenKeyPairResponse> callGenGetKeyPairById(@PathVariable String Id,Principal principal) {
        // Use WebClient to send a GET request to the endpoint /Gengetkeypair/{Id}
        String userInfo = getUserId(principal);
        String userId = userName(SecurityContextHolder.getContext().getAuthentication());
        String ownerID=Owner_ID(SecurityContextHolder.getContext().getAuthentication());

        if (!"User not authenticated".equals(userInfo) && !"User not available".equals(userId)) {

            DefaultOidcUser oidcUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String tokenValue = oidcUser.getIdToken().getTokenValue();

            ResponseEntity<GenKeyPairResponse> responseEntity = webClient.get()
                    .uri("http://KEYEX-SERVICE/api/key/Gengetkeypair/{Id}", Id)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
                    .retrieve()
                    .toEntity(GenKeyPairResponse.class)
                    .block();
            return responseEntity;

        }
        else
        {
            System.out.println("User not authenticated or user ID not available");
            return null;

        }
    }

    @GetMapping("/callGenDelkeypair/{Id}")
    public ResponseEntity<String> callGenDeleterKeyPairById(@PathVariable String Id,Principal principal) {
        // Use WebClient to send a GET request to the endpoint /GenDelkeypair/{Id}
        String userInfo = getUserId(principal);
        String userId = userName(SecurityContextHolder.getContext().getAuthentication());
        String ownerID=Owner_ID(SecurityContextHolder.getContext().getAuthentication());

        if (!"User not authenticated".equals(userInfo) && !"User not available".equals(userId)) {

            DefaultOidcUser oidcUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String tokenValue = oidcUser.getIdToken().getTokenValue();

            ResponseEntity<String> responseEntity = webClient.get()
                    .uri("http://KEYEX-SERVICE/api/key/GenDelkeypair/{Id}", Id)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
                    .retrieve()
                    .toEntity(String.class)
                    .block();
            return responseEntity;

        }
        else
        {
            System.out.println("User not authenticated or user ID not available");
            return null;

        }
    }
    @GetMapping("/callGenfindall")
    public ResponseEntity<List<GenKeyPairResponse>> callGenFindAll(Principal principal) {
        // Use WebClient to send a GET request to the endpoint /Genfindall
        String userInfo = getUserId(principal);
        String userId = userName(SecurityContextHolder.getContext().getAuthentication());
        String ownerID=Owner_ID(SecurityContextHolder.getContext().getAuthentication());

        if (!"User not authenticated".equals(userInfo) && !"User not available".equals(userId)) {

            DefaultOidcUser oidcUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String tokenValue = oidcUser.getIdToken().getTokenValue();
            ResponseEntity<List<GenKeyPairResponse>> responseEntity = webClient.get()
                    .uri("http://KEYEX-SERVICE/api/key/Genfindall")
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
                    .retrieve()
                    .toEntityList(GenKeyPairResponse.class)
                    .block();
            return responseEntity;

        }
        else
        {
            System.out.println("User not authenticated or user ID not available");
            return null;

        }
    }








}
