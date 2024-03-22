package com.productcnit.controller;

import com.productcnit.dto.*;
import com.productcnit.service.WebSocketService;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/socket")
@CrossOrigin("*")
public class DataController {

    @Autowired
    private final WebSocketService webSocketService;

    public DataController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @GetMapping("/data")
    public ResponseEntity<String> getData(@RequestParam("privatekey") String privateKey) {
        String secretMessage = webSocketService.getData(privateKey);
        if (secretMessage != null) {
            return ResponseEntity.ok(secretMessage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data");
        }
    }


    @GetMapping("/getkeypair")
    public ResponseEntity<GenKeyPairResponse> getkeypair(String Recid, Authentication authentication) {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        Map<String, Object> claims = jwt.getClaims();
        String Ownerid = (String) claims.get("Owner_ID");
        System.out.println("OwnerId"+Ownerid);
        GenKeyPairResponse secretMessage = webSocketService.getkeypair(authentication,Ownerid);
        PublicKeyMessageSend ResMessage= new PublicKeyMessageSend();
        ResMessage.setPublicKey(secretMessage.getGen_public_Key());
        ResMessage.setSenderId(secretMessage.getGen_Owner_Id());
        ResMessage.setRecId(Recid);
        webSocketService.SendPublicKey(ResMessage);
        if (secretMessage != null) {
            return ResponseEntity.ok(secretMessage);
        } else {
            System.out.println("error in getkeypair");
            return null;
        }
    }

    @GetMapping("/getsharedkey")
    public ResponseEntity<String> Sharedkey(@RequestParam("Recid") String Recid,@RequestParam("SenderId") String SenderId, @RequestParam("publicKey") String publicKey, Authentication authentication)
     {
         String secretMessage = webSocketService.Sharedkey(Recid,SenderId,publicKey,authentication);
         if (secretMessage != null) {
             return ResponseEntity.ok(secretMessage);
         } else {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data");
         }


     }
    @PostMapping("/SendPubKey")
    public void SendPublicKey(@RequestBody final PublicKeyMessageSend publicKeyMessage)
    {
        webSocketService.SendPublicKey(publicKeyMessage);
    }

    @PostMapping("/SendEncMessage")
    public void SendEncMessage(@RequestBody final EncMessageResponse encMessageResponse)
    {
        webSocketService.SendEncMessage(encMessageResponse);
    }

    @GetMapping("/getDecryptmessage")
    public DecMessage getDecrypt(@RequestParam("encryptedMessage") String encryptedMessage,@RequestParam("senderid") String senderid,
                                 @RequestParam("secretkey") String secretkey, @RequestParam("peerid") String peerid)

    {
        String SecretKey = secretkey;
        return webSocketService.getDecrypt(encryptedMessage,senderid,peerid,secretkey);
    }

    @GetMapping("/getMessageDecrypted")
    public DecMessage getMessageDecrypted(@RequestParam("encryptedMessage") String encryptedMessage,@RequestParam("senderid") String senderid,
                                 @RequestParam("secretkey") String secretkey, @RequestParam("peerid") String peerid)

    {
        System.out.println("encryptedMessage to be decrypted in websocket"+encryptedMessage);
        String SecretKey = secretkey;
//        String encryptedMessage1 = URLDecoder.decode(encryptedMessage, StandardCharsets.UTF_8);
        System.out.println("encryptedMessage1 to be decrypted in websocket"+encryptedMessage);
        return webSocketService.getMessageDecrypted(encryptedMessage,senderid,peerid,secretkey);
    }

    @GetMapping("/Encrypt")
    public EncMessageResponse GetEncrypt(@RequestParam("encryptedMessage") String encryptedMessage, @RequestParam("senderid") String senderid,
                                 @RequestParam("secretkey") String secretkey, @RequestParam("peerid") String peerid)

    {
        System.out.println("message to be encrypted"+encryptedMessage);

//        String encryptedMessage1 = URLDecoder.decode(encryptedMessage, StandardCharsets.UTF_8);
        System.out.println("message to be encrypted1"+encryptedMessage);
        String SecretKey = secretkey;
        return webSocketService.getEncrypt(encryptedMessage,senderid,peerid,secretkey);
    }




}
