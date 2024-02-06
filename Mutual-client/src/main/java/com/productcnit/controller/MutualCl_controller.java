package com.productcnit.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/client")
public class MutualCl_controller {

    @Autowired
    WebClient webClient;

    @GetMapping("/http")
    public String server() {

        WebClient webClient1 = WebClient.create("http://localhost:8082");
        return  webClient1.get().uri("/server").retrieve()
                .bodyToMono(String.class).block();
    }

    @GetMapping("/https")
    public String gatherDataFromServer() {
        Mono<String> dateFromServer = webClient.get()
                .uri("https://localhost:8082/server")
                .retrieve().bodyToMono(String.class);
        return dateFromServer.block();
    }


}
