package com.productcnit.Service;


import com.productcnit.dto.ShipRouting;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;


@Slf4j
@Service
public class ShipA_Service {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public String SendRouting(String Recid, String SenderId, String publicKey, Authentication authentication) {
        WebClient webClient = WebClient.create("http://SHORE-SERVICE");
        System.out.println("publickey"+publicKey);
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Map<String, Object> claims = jwt.getClaims();
        String OwnerId = (String) claims.get("Owner_ID");
        String  userId =authentication.getName();
        WebClient webClient1 = webClientBuilder.build();
        String response = webClient1.get()
                .uri(builder -> {
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                            .scheme("http")
                            .host("SHORE-SERVICE")
                            .path("/api/Shore/sharedkey")
                            .queryParam("SenderId", URLEncoder.encode(SenderId, StandardCharsets.UTF_8))
                            .queryParam("Recid", URLEncoder.encode(Recid, StandardCharsets.UTF_8))
                            .queryParam("publicKey", URLEncoder.encode(publicKey, StandardCharsets.UTF_8));
                    return uriBuilder.build().toUri();
                })
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }
    public String processShipRouting(ShipRouting shipRouting,Authentication authentication) {
        // Implement your business logic here
        // For example, save the ShipRouting details to a database
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Map<String, Object> claims = jwt.getClaims();
        String OwnerId = (String) claims.get("Owner_ID");
        String  userId =authentication.getName();
        System.out.println("Processing  ownerid: " + OwnerId);
        System.out.println("Processing ship routing: " + shipRouting);
        return "Processed successfully";
    }
}
