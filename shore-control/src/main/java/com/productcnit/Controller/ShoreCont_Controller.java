package com.productcnit.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.productcnit.Service.ShoreCont_Service;
import com.productcnit.dto.GenKeyPairResponse;
import com.productcnit.dto.SenRecResponse;
import com.productcnit.dto.ShipRouting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/Shore")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ShoreCont_Controller {

//    private final WebClient webClient = WebClient.builder().build();

    @Autowired
    private WebClient.Builder webClientBuilder;
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final KafkaTemplate<String, SenRecResponse> kafkaloginTemplate;
    private final ShoreCont_Service shoreContService;


    @PostMapping("/route")
    public ResponseEntity<String> createShipRouting(@RequestBody ShipRouting shipRouting, Authentication authentication) {
        // Use the service to process the ShipRouting object
        String response=shoreContService.processShipRouting(shipRouting,authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sendroute")
    public ResponseEntity<String> sendRouting(@RequestParam("RecId") String RecId, @RequestParam("secretkey") String secretkey,Authentication authentication) throws JsonProcessingException {
        // Extract data from request DTO
        String response = shoreContService.SendRouting(RecId,secretkey,authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/home")
    public String home()
    {
        return "example";
    }

    @GetMapping("/home2")
    public ResponseEntity<String> home2() {
        String response = "hello";
        return ResponseEntity.ok(response);
    }
    @GetMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        String userId = "";
        String ownerid = "";

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
            userId =authentication.getName();
            ownerid = (String) claims.get("Owner_ID"); // Adjust claim key if needed
            System.out.println("ownerid"+ ownerid);
            System.out.println("userId"+ userId);
        } catch (Exception e) {
            //log.error("Error retrieving user information from JWT:", e);
        }
        SenRecResponse senRecResponse= new SenRecResponse(ownerid,userId);
        kafkaloginTemplate.send("Save-Gen-keypair-topic", "Gen-key-pair", senRecResponse);
            return ResponseEntity.ok("OwnerId sent  successfully");
    }



    @GetMapping("/userId2")
    private String  getUserId2(Authentication authentication) {
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
            System.out.println("email"+ email);

            response.put("email", email);
            response.put("username", username); // You can add more profile details as needed

            return ownerid;
        } catch (Exception e) {
            //log.error("Error retrieving user information from JWT:", e);
            response.put("error", "Failed to retrieve user information");
            return "error faild to retrive";
        }
    }

    @GetMapping("/sendpub1")
    public String Sendpub1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            String tokenValue = oidcUser.getIdToken().getTokenValue();
            WebClient webClient =webClientBuilder.build();
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

    @GetMapping("/getkeypair/{OwnerId}")
    public GenKeyPairResponse getkeypair(String OwnerId)
    {
        try
        {
            WebClient webClient = WebClient.create("http://KEYEX-SERVICE");

            // Build the request
            WebClient.RequestHeadersSpec<?> requestSpec = webClient
                    .method(HttpMethod.GET)
                    .uri("/api/key/Gengetkeypair"+"/"+OwnerId);

            // Execute the request
            Mono<GenKeyPairResponse> responseMono = requestSpec.retrieve().bodyToMono(GenKeyPairResponse.class);

            // Block and get the response
            GenKeyPairResponse response = responseMono.block();

            String privatekey= response.getGen_private_Key();
            String publickey= response.getGen_public_Key();
            String ownerid= response.getGen_Owner_Id();
            String userid= response.getGen_User_Id();
            System.out.println("privatekey"+privatekey);
            System.out.println("publickey"+publickey);
            System.out.println("ownerid"+ownerid);
            System.out.println("userid"+userid);
            ;
//            System.err.println("The Secret Message\n"+secretMessage);
            return response;
        }
        catch (Exception ignored)
        {
            return null;
        }

    }

    @GetMapping("/getstring")
    public String getstring(Authentication authentication)
    {
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            System.out.println(jwt.getTokenValue());
        WebClient webClient =webClientBuilder.build();
          String response= webClient.get()
                    .uri("http://KEYEX-SERVICE/api/key/OwnerID")
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return response;

    }


}
