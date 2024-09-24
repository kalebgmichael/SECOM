package com.productcnit.controller;

import com.productcnit.dto.DecMessage;
import com.productcnit.dto.EncKeyResponse;
import com.productcnit.dto.EncMessage;
import com.productcnit.repository.EncKeyRepository;
import com.productcnit.service.DecSymService;
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
@RequestMapping("/api/decsym")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DecSymController {

    private final DecSymService decSymService;

    WebClient webClient2= WebClient.create();
    @Autowired
    private WebClient.Builder webClientBuilder;

    private final EncKeyRepository encKeyRepository;

    @GetMapping("getmsg")
    public String getdata()
    {
        return  decSymService.getData();
    }

    @GetMapping("getDecrypt")
    public String getDecrypt()
    {
        return  decSymService.getData();
    }

    @GetMapping("/getDecryptmessage")
    public DecMessage getDecrypt(@RequestParam("encryptedMessage") String encryptedMessage,@RequestParam("senderid") String senderid,
                                 @RequestParam("secretkey") String secretkey, @RequestParam("peerid") String peerid)

    {
        System.out.println("this is secretkey"+ secretkey );
//        String SecretKey = secretkey.replaceAll("^\"|\"$", ""); // Remove leading and trailing quotation marks
        String SecretKey = secretkey;
        System.out.println("this is secretkey"+ SecretKey );
        return  decSymService.getDecrypt(encryptedMessage,senderid,peerid,secretkey);
    }

    @GetMapping("/Decrypt")
    public DecMessage Decrypt(@RequestParam("encryptedMessage") String encryptedMessage,@RequestParam("senderid") String senderid,
                              @RequestParam("peerid") String peerid,@RequestParam("secrectkey") String secrectkey)
    {
        decSymService.initFromStrings("CHuO1Fjd8YgJqTyapibFBQ==", "e3IYYJC2hxe24/EO");

        String decryptedmessage= decSymService.decrypt(encryptedMessage);
        DecMessage decMessage= new DecMessage();
        decMessage.setMessage(decryptedmessage);
        decMessage.setSenderId(senderid);
        decMessage.setRecId(peerid);
        return decMessage;
    }
    @GetMapping("/GetDecrypt")
    public DecMessage GetDecrypt(@RequestParam("message") String message, @RequestParam("secretkey") String secretkey,
                                 @RequestParam("sendid") String sendid, @RequestParam("peerid") String peerid, Authentication authentication)
    {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        WebClient webClient1 = webClientBuilder.build();
        String message1 = URLDecoder.decode(message, StandardCharsets.UTF_8);
//        String message1 = message;
        String secretkey1 = URLDecoder.decode(secretkey, StandardCharsets.UTF_8);
        String sendid1 = URLDecoder.decode(sendid, StandardCharsets.UTF_8);
        String peerid1 = URLDecoder.decode(peerid, StandardCharsets.UTF_8);

        System.out.println("secretkey is " + secretkey1);
        System.out.println("message1 in encryptsym is " + message1);
        System.out.println("message in encryptsym is " + message);
        System.out.println("message in sendid1 is " + sendid1);
        System.out.println("message in peerid1 is " + peerid1);
        // Add double quotes around sendid1 and peerid1
        String quotedSendid1 = "\"" + sendid1 + "\"";
        String quotedPeerid1 = "\"" + peerid1 + "\"";

        System.out.println("sendid1 with quotes is " + quotedSendid1);
        System.out.println("peerid1 with quotes is " + quotedPeerid1);
//        String message2 = message1.replaceAll(
//        "|\"$", "");
        EncKeyResponse[] sharedkeys = encKeyRepository.findByOwnerAndPairId("00001", "00002").toArray(new EncKeyResponse[0]);
        EncKeyResponse[] sharedkeys3 = encKeyRepository.findByOwnerAndPairId(sendid, peerid).toArray(new EncKeyResponse[0]);
        EncKeyResponse[] sharekeys2= encKeyRepository.findAll1().toArray(new EncKeyResponse[0]);
        System.out.println("this is shared keys"+sharekeys2[2].getEnc_Key());
        String enc_sharedkey= sharedkeys[0].getEnc_Key();
//        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8084/get_enc_sig_verif")
//                .queryParam("encryptedmessage", URLEncoder.encode(enc_sharedkey, StandardCharsets.UTF_8))
//                .build()
//                .toUri();
//
//        String response = webClient2.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
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
//        String SecretKey = "9f7F8bM+5AS7kj/4a0A1kQ==";
        decSymService.initFromStrings(response, "e3IYYJC2hxe24/EO");
        String decryptedmessage= decSymService.decrypt(message1);
        System.out.println("dectryptedmessage in GetDncrypt is " + decryptedmessage);
        DecMessage decMessage = new DecMessage();
        decMessage.setMessage(decryptedmessage);
        decMessage.setSenderId(sendid1);
        decMessage.setRecId(peerid1);
        return decMessage;
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
