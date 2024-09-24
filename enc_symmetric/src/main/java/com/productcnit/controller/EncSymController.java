package com.productcnit.controller;


import com.productcnit.dto.EncKeyResponse;
import com.productcnit.dto.EncMessage;
import com.productcnit.repository.EncKeyRepository;
import com.productcnit.service.EncSymService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/encsym")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EncSymController {

    private final EncSymService encSymService;
    @Autowired
    private EncKeyRepository encKeyRepository;

    private final WebClient webClient = WebClient.builder().build();
    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/getsecret")
    public String getdata()
    {
        encSymService.initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==", "e3IYYJC2hxe24/EO");

        return encSymService.encrypt("kalebCyber");
    }

    @GetMapping("/Encrypt")
    public EncMessage Encrypt(@RequestParam("message") String message, @RequestParam("secretkey") String secretkey,
                              @RequestParam("sendid") String sendid, @RequestParam("peerid") String peerid, Authentication authentication)
    {

//        try
//        {

            WebClient webClient1 = webClientBuilder.build();
           Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            WebClient webClient2= WebClient.create();
            String message1 = URLDecoder.decode(message, StandardCharsets.UTF_8);
//        String message1 = message;
            String secretkey1 = URLDecoder.decode(secretkey, StandardCharsets.UTF_8);
            String sendid1 = URLDecoder.decode(sendid, StandardCharsets.UTF_8);
            String peerid1 = URLDecoder.decode(peerid, StandardCharsets.UTF_8);

            System.out.println("secretkey is " + secretkey1);
            System.out.println("message1 in encryptsym is " + message1);
            System.out.println("message in encryptsym is " + message);

            EncKeyResponse[] sharedkeys = encKeyRepository.findByOwnerAndPairId(sendid, peerid).toArray(new EncKeyResponse[0]);
            String enc_sharedkey= sharedkeys[0].getEnc_Key();
//            URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8084/get_enc_sig_verif")
//                    .queryParam("encryptedmessage", URLEncoder.encode(enc_sharedkey, StandardCharsets.UTF_8))
//                    .build()
//                    .toUri();
//
//            String response = webClient2.get()
//                    .uri(uri)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
        String response = webClient1.get()
                .uri(builder -> {
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                            .scheme("http")
                            .host("DECKEY-SERVICE")
                            .path("/api/deckey/get_enc_sig_verif")
                            .queryParam("encryptedmessage", URLEncoder.encode(enc_sharedkey, StandardCharsets.UTF_8));
                    return uriBuilder.build().toUri();
                })
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
            System.out.println("decrypted sharedkey is"+response);
//        String message2 = message1.replaceAll(
//        "|\"$", "");
            String SecretKey = secretkey1;
            encSymService.initFromStrings(response, "e3IYYJC2hxe24/EO");
            String encryptedmessage= encSymService.encrypt(message1);
            System.out.println("encryptedmessage in encryptsym is " + encryptedmessage);
            EncMessage encMessage = new EncMessage();
            encMessage.setMessage(encryptedmessage);
            encMessage.setSenderId(sendid);
            encMessage.setRecId(peerid);
            return encMessage;
//        }
//        catch (Exception ignored){
//            System.out.println("error in Encrypt method");
//            return null;
//        }


    }

    //Enckey Repository info
    @PostMapping("/EncKeySave")
    public EncKeyResponse SaveGen(@RequestBody EncKeyResponse encKeyResponse) {
        try {
            return encKeyRepository.save(encKeyResponse);
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/GetEncKey/{Id}")
    public EncKeyResponse FindEncKey(@PathVariable String Id) {
        try {
            EncKeyResponse keys = encKeyRepository.findKeypairbyId(Id);
            System.out.println("this is ownerid " + keys.getOwner_Id() + "this is pair " + keys.getPair_Id());
            return keys;
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/DelEncKey/{Id}")
    public String DeleteEncKey(@PathVariable String Id) {
        try {
            String keys = encKeyRepository.deletekeypair(Id);
            return "keys deleted successfully";
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/EncKeyfindall")
    public List<Object> EncKeyfindall() {
        try {
            return encKeyRepository.findall();
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/keys")
    public List<EncKeyResponse> findByOwnerAndPairId(
            @RequestParam String Owner_Id,
            @RequestParam String Pair_Id) {
        return encKeyRepository.findByOwnerAndPairId(Owner_Id, Pair_Id);
    }

    @GetMapping("/sharedkey_pair")
    public String findByOwnerAndPairId_sharedkey(
            @RequestParam String Owner_Id,
            @RequestParam String Pair_Id) {
        EncKeyResponse[] sharedkeys = encKeyRepository.findByOwnerAndPairId(Owner_Id, Pair_Id).toArray(new EncKeyResponse[0]);
        String sharedkey= sharedkeys[0].getEnc_Key();
        return sharedkey;
    }
}
