package com.productcnit.Controller;


import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/key")
public class KeyexchangeController {

    @GetMapping
    public String getpublickey()
    {
        try
        {
            WebClient webClient = WebClient.create("http://localhost:8083");

            // Build the request
            WebClient.RequestHeadersSpec<?> requestSpec = webClient
                    .method(HttpMethod.GET)
                    .uri("/getsig");

            // Execute the request
            Mono<String> responseMono = requestSpec.retrieve().bodyToMono(String.class);

            // Block and get the response
            String response = responseMono.block();
            String signaturemessage= response.toString();
            System.out.println(signaturemessage);
            String kalex= "kaleb";
            // Process the response as needed
//        System.out.println("Response: " + response);
            boolean secretMessage= verifySignature(kalex,signaturemessage);
//            System.err.println("The Secret Message\n"+secretMessage);
            return secretMessage;
        }
        catch (Exception ignored)
        {
            System.out.println("error");
            return false;
        }
    }
}
