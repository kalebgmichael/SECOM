package com.productcnit.Service;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;


@Slf4j
@Service
public class ShipA_Service {

    private final String dhServiceUrl = "http://localhost:8085/dh-service";


    public String initiateKeyExchange() {
        // Communicate with the Diffie-Hellman microservice to initiate the key exchange
        String response = new RestTemplate().getForObject(dhServiceUrl + "/initiateKeyExchange", String.class);

        // Decode the base64-encoded response
//        byte[] decodedBytes = Base64.getDecoder().decode(response);
//        String decodedResponse = new String(decodedBytes);

        // Log the decoded response for debugging purposes
       System.out.println("Decoded Response: " + response);

        // Extract the server's public key from the response using JSON parsing
        //String serverPublicKey = extractServerPublicKey(response);

        // Use the server's public key for subsequent key exchange steps if needed

        return response;
    }


    public String completeKeyExchange(String clientPublicKey) {
        // Communicate with the Diffie-Hellman microservice to complete the key exchange

        return new RestTemplate().getForObject(dhServiceUrl + "/completeKeyExchange?clientPublicKey={clientPublicKey}", String.class, clientPublicKey);
    }

    private String extractServerPublicKey(String response) {
        // Use org.json library to parse the JSON response
        JSONObject jsonResponse = new JSONObject(response);

        // Extract the server's public key
        String serverPublicKey = jsonResponse.getString("serverPublicKey");

        return serverPublicKey;
    }

    private String encode(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data)
    {
        return  Base64.getDecoder().decode(data);
    }
}
