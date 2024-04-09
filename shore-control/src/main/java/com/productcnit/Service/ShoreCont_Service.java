package com.productcnit.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productcnit.dto.EncMessage;
import com.productcnit.dto.EncMessageResponse;
import com.productcnit.dto.GenKeyPairResponse;
import com.productcnit.dto.ShipRouting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class ShoreCont_Service {
    @Autowired
    private WebClient.Builder webClientBuilder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String processShipRouting(ShipRouting shipRouting, Authentication authentication) {
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


    public String SendRouting(String recId,String secretkey, Authentication authentication) throws JsonProcessingException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Map<String, Object> claims = jwt.getClaims();
        String OwnerId = (String) claims.get("Owner_ID");
        System.out.println("OwnerId: " + OwnerId);
        String userId = authentication.getName();
        String lon= "1";
        String lat= "2";
        WebClient webClient1 =webClientBuilder.build();
        ShipRouting sharedKeyDetails = new ShipRouting(OwnerId, userId, lon,lat);
        String json= objectMapper.writeValueAsString(sharedKeyDetails);
        System.out.println("json of object"+json);
        EncMessageResponse response1= getEncrypt(json,OwnerId,recId,secretkey);

        String response = webClient1.post()
                .uri("http://SHIP-SERVICE/api/ship/route")
                .header("Authorization", "Bearer " + jwt.getTokenValue())
                .bodyValue(sharedKeyDetails)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

    public EncMessageResponse getEncrypt(String Message, String senderid,
                                         String peerid, String secretkey){

        String SecretKey = secretkey;
        WebClient webClient1 = webClientBuilder.build();
        WebClient webClient2= WebClient.create();

        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8085/Encrypt")
                .queryParam("message", URLEncoder.encode(Message, StandardCharsets.UTF_8))
//                .queryParam("message",Message)
                .queryParam("secretkey", URLEncoder.encode(secretkey, StandardCharsets.UTF_8))
                .queryParam("sendid", URLEncoder.encode(senderid, StandardCharsets.UTF_8))
                .queryParam("peerid", URLEncoder.encode(peerid, StandardCharsets.UTF_8))
                .build()
                .toUri();

        EncMessage response = webClient2.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(EncMessage.class)
                .block();

        EncMessageResponse encMessage = new EncMessageResponse();
        encMessage.setMessage(response.getMessage());
        encMessage.setSenderId(response.getSenderId());
        encMessage.setRecId(response.getRecId());
        encMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        //send encryptedmessage
//        SendEncMessage(encMessage);
        return encMessage;

    }


}
