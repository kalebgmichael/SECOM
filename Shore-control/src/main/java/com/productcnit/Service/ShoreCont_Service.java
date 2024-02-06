package com.productcnit.Service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

@Service
public class ShoreCont_Service {

    private final String dhServiceUrl = "http://localhost:8085/dh-service";

    public String initiateKeyExchange() throws NoSuchAlgorithmException {
        // Communicate with the Diffie-Hellman microservice to initiate the key exchange
        return new RestTemplate().getForObject(dhServiceUrl + "/initiateKeyExchange", String.class);
    }

    public String completeKeyExchange(String serverPublicKey) throws Exception {
        // Communicate with the Diffie-Hellman microservice to complete the key exchange
        return new RestTemplate().getForObject(dhServiceUrl + "/completeKeyExchange?clientPublicKey={clientPublicKey}", String.class, serverPublicKey);
    }

}
