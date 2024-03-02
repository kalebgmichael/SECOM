package com.productcnit.Controller;


import com.nimbusds.jose.shaded.gson.Gson;
import com.productcnit.Service.KeyManager;
import com.productcnit.dto.GenKeyPairResponse;
import com.productcnit.dto.KeyPairResponse;
import com.productcnit.dto.PublicKeyMessage;
import com.productcnit.dto.SenRecResponse;
import com.productcnit.model.KeyPair;
import com.productcnit.repository.GeneralKeyPairRepository;
import com.productcnit.repository.KeyPairRespository;
import jakarta.servlet.http.HttpSession;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/key")
@CrossOrigin("*")
public class KeyexchangeController {


    String sendid= "ship0";
    String recid="shore0";
    private  String PUBLIC_KEY_STRINGS="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";
    private  String PRIVATE_KEY_STRINGS="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIBAtCMdVcFMVgV/OUgq+4EJfqZmYBB+8akBVIaGddn2DdOFrgUqyL0FNSXPbPA+2TMg0t9RghE9/vVDmEKThjtABwkfcC+JOcJqwDAbsoEPMZgUz+RrnaSKiX/S8xVKV7Wj131lPMX72kn4aSgXnKQUXrbLuavH3KikVK5x7NoJAgMBAAECgYA2uWUjxpSc0jGyTsLmZFDEkoSUBALhhwkekA69CAqpYjAsHVJPqh3Vaa9v3r4hFPAgvNS9rU3OhaGQjbMeVUxkx9GVt2LUnGMX/M5yFh7OZl7cMtebhoeEt0z5SXI3NULiEKKjHgXwTc4DaA+lcp/gxSNDQNlSFJWhfvTUQDqosQJBAJ8nPN0eOesTaCCzATO3FpkcuhQ9XLhAQTvbzYb67gtK+4Vh8pBAEFLEuKZAmBidub63Lv50cLnm/1L7+4Y6+H8CQQDOS871vu+n914il1GBZ81IqbwKki3IWx/d6iXz1UlXyOEMgniNpQYKW8EWzvrT/lXDlTe3VKgpBfllpgIBySl3AkBzmfmglxr0wDTrQ3qFCOEWOAKFPwkBIFMB2qdP+yY696z4dmvNEWuJ4zBIOjT/9Fj9yWsOEp/quHoO2c8Z8e2bAkB05i5bwRuq8ZjNPzP3gWupXk1pLBZ3b3OqW7Gv70/FR9aHMTPBCB9ZJU9Qbm9iS8AruVW+NGGqBXGisSR4AJbXAkAjPxbNZsSfeJZY/AkU2SNbIvHnLACQvRwJwibvzAkDfJ4t1gVohi+enBO91slbMT+tQGD9qnLxDJpso4B3rmSG";



    private RestTemplate restTemplate;



    private Authentication authentication;


    private final WebClient webClient = WebClient.builder().build();
    private String privateKey;
    private String publicKey;
    KeyPairResponse keyPairResponse;

    @Autowired
    private final KeyManager keyManager;

    @Autowired
    private KeyPairRespository keyPairRespository;

    @Autowired
    private GeneralKeyPairRepository generalKeyPairRepository;
    @Autowired
    private final HttpSession session;

    private final KafkaTemplate<String, PublicKeyMessage> kafkaTemplate;
    private final KafkaTemplate<String, GenKeyPairResponse> kafkaTemplate1;



    public KeyexchangeController(KeyManager keyManager, HttpSession session, KafkaTemplate<String, PublicKeyMessage> kafkaTemplate, KafkaTemplate<String, GenKeyPairResponse> kafkaTemplate1) {
        this.keyManager = keyManager;
        this.session = session;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate1 = kafkaTemplate1;
        this.restTemplate=new RestTemplate();
    }


    @GetMapping("/getpair")
    public KeyPairResponse getKeyStrPrv() {
        return new KeyPairResponse(keyPairResponse.getPublickey(), keyPairResponse.getPrivatekey());
    }
    @GetMapping("/getkeypair1")
    public KeyPairResponse getpairkey1() {
        KeyManager.generateKeyPair();
        privateKey = keyManager.generatePrviatekey();
        publicKey = keyManager.generatePublicKey();
        keyPairResponse= new KeyPairResponse(publicKey,privateKey);

        return keyPairResponse;
    }

    @GetMapping("/sendpub")
    public ResponseEntity<String> getPublicKey() {
        String PUBLIC_KEY_STRINGS1="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAQLQjHVXBTFYFfzlIKvuBCX6mZmAQfvGpAVSGhnXZ9g3Tha4FKsi9BTUlz2zwPtkzINLfUYIRPf71Q5hCk4Y7QAcJH3AviTnCasAwG7KBDzGYFM/ka52kiol/0vMVSle1o9d9ZTzF+9pJ+GkoF5ykFF62y7mrx9yopFSucezaCQIDAQAB";

        PublicKeyMessage publicKeyMessage = new PublicKeyMessage("ship0", PUBLIC_KEY_STRINGS1,"shore0");
            kafkaTemplate.send("key-pair-topic", "client-idx", publicKeyMessage);
            return ResponseEntity.ok("Public key published successfully");

    }

    @KafkaListener(topics = "key-pair-topic", groupId = "group-id1")
    public KeyPairResponse getpairkey(PublicKeyMessage publicKeyMessage) {
        System.out.println(publicKeyMessage.getRecId());




        String clientidx= publicKeyMessage.getRecId();
        if(clientidx.equals(recid))
        {
            KeyManager.generateKeyPair();
            privateKey = keyManager.generatePrviatekey();
            publicKey = keyManager.generatePublicKey();
//            session.setAttribute("privateKeyx", privateKey);
//            session.setAttribute("publicKeyx", publicKey);
            System.out.println("Private Key: " + privateKey);
            System.out.println("Public Key: " + publicKey);
            keyPairResponse= new KeyPairResponse(publicKey,privateKey);


            return keyPairResponse;
        }
        else
        {
            System.out.println("no clientid");
            return null;
        }

    }

    @KafkaListener(topics = "Save-Gen-keypair-topic", groupId = "group-id3")
    public KeyPairResponse SaveGenKeyPair(SenRecResponse senRecResponse) {

        String CurrentuserId = userName(SecurityContextHolder.getContext().getAuthentication());
        System.out.println("currentid "+CurrentuserId);
        String Owner_ID = senRecResponse.getGen_Owner_Id();
        String userId = senRecResponse.getGen_User_Id();
        System.out.println("this userid "+userId);

        GenKeyPairResponse keys= generalKeyPairRepository.findKeypairbyId(Owner_ID);
        if(keys != null)
        {

          System.out.println("Data already exists in cache");
          return null;
        }
        else
        {
            KeyManager.generateKeyPair();
            String privateKey = keyManager.generatePrviatekey();
            String publicKey = keyManager.generatePublicKey();
//            session.setAttribute("privateKeyx", privateKey);
//            session.setAttribute("publicKeyx", publicKey);
            System.out.println("Private Key: " + privateKey);
            System.out.println("Public Key: " + publicKey);
            keyPairResponse= new KeyPairResponse(publicKey,privateKey);
            // Save the generated key pair to the repository
            GenKeyPairResponse genKeyPairResponse= new GenKeyPairResponse(Owner_ID,userId,privateKey,publicKey);
            generalKeyPairRepository.save(genKeyPairResponse);
            System.out.println("Saved data to cache by "+ userId);


            return keyPairResponse;
        }

    }

    @GetMapping("/getownerid")
    public String getOwnerId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String tokenValue = jwt.getTokenValue();
        //System.out.println(tokenValue);
        String ownerid = getUserId2(authentication);
        System.out.println("ownerid" + ownerid);
        // Make a GET request to obtain the owner ID
//        String ownerId = restTemplate.getForObject("http://localhost:8080/api/key/userId2", String.class);
        // Retrieve current logged-in user's ID
        String loggedInUserId = authentication.getName(); // Assuming Keycloak is integrated for authentication
        System.out.println("loggedInUserId"+loggedInUserId);


        String ownerId = webClient.get()
                .uri("http://localhost:8080/api/Shore/userId2")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "This is a message from keyexchange: " + ownerId;
    }

//        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
//        String tokenValue = oidcUser.getIdToken().getTokenValue();
//
//        String response = webClient.get()
//                .uri("http://localhost:8080/api/key/userId2")
//                .headers(httpHeaders -> httpHeaders.setBearerAuth(tokenValue))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        return "This is message from keyexchange " + response;
//        String authToken = ((JwtAuthenticationToken) authentication).getToken().getTokenValue();
//
//        String response = webClient.get()
//                .uri("http://localhost:8080/api/key/userId2")
//                .headers(headers -> headers.setBearerAuth(authToken))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        System.out.println(response);
//        return response;

    private String getToken(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            System.out.println("this is the token response");
            return ((JwtAuthenticationToken) authentication).getToken().getTokenValue();
        } else {
            throw new IllegalArgumentException("Authentication is not JWT");
        }
    }

    @GetMapping("/userId2")
    private String  getUserId2(Authentication authentication) {
//        Map<String, String> response = new HashMap<>();

        try {
            if (!(authentication instanceof JwtAuthenticationToken)) {
                throw new SecurityException("Invalid authentication type");
            }

            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            Map<String, Object> claims = jwt.getClaims();

            System.out.println("claims"+claims);

            // Extract profile and email claims
            String email = (String) claims.get("email"); // Adjust claim key if needed
            String firstName = (String) claims.get("firstName"); // Adjust claim key if needed
            String lastName = (String) claims.get("lastName"); // Adjust claim key if needed

            // Combine for a username-like identifier (optional)
            String username = String.format("%s %s", firstName, lastName);
            String ownerid = (String) claims.get("Owner_ID"); // Adjust claim key if needed
            System.out.println("ownerid"+ ownerid);
            System.out.println("email"+ email);
//
//            response.put("email", email);
//            response.put("username", username); // You can add more profile details as needed

            return ownerid;
        } catch (Exception e) {
            //log.error("Error retrieving user information from JWT:", e);
//            response.put("error", "Failed to retrieve user information");
            return "error faild to retrive";
        }
    }

    @KafkaListener(topics = "key-pair-topic", groupId = "group-id2")
    public String getsecsharedkey(PublicKeyMessage publicKeyMessage) {
//        System.out.println("private"+privateKey);
//        System.out.println("public"+publicKey);
        String peerPublicKey = publicKeyMessage.getPublicKey();
        keyManager.initFromStringsPublickey(publicKey);
        keyManager.initFromStringsPrvkey(privateKey);
        String sharedKey = keyManager.generateSharedSecret();
        System.out.println("the shared key is "+sharedKey);
        return sharedKey;
    }

   @PostMapping("/savekeypair")
    public KeyPair save(@RequestBody KeyPair keyPair)
   {
       return keyPairRespository.save(keyPair);
   }
   @GetMapping("/getkeypair/{Id}")
    public KeyPair findkeypair(@PathVariable String Id)
   {
       KeyPair keys= keyPairRespository.findKeypairbyId(Id);

       System.out.println("this is private "+keys.getPrivateKey()+"this is public "+keys.getPublicKey());
       return keys;
   }
   @GetMapping("/findall")
    public List<Object> findall()
   {
       return keyPairRespository.findall();
   }

    @PostMapping("/GenSave_keypair")
    public GenKeyPairResponse SaveGen(@RequestBody GenKeyPairResponse genKeyPairResponse)
    {
        return generalKeyPairRepository.save(genKeyPairResponse);
    }
    @GetMapping("/Gengetkeypair/{Id}")
    public GenKeyPairResponse findGenkeypair(@PathVariable String Id)
    {
        GenKeyPairResponse keys= generalKeyPairRepository.findKeypairbyId(Id);

        System.out.println("this is private "+keys.getGen_private_Key()+"this is public "+keys.getGen_public_Key());
        return keys;
    }
    @GetMapping("/GenDelkeypair/{Id}")
    public String DeleteGenkeypair(@PathVariable String Id)
    {
        String keys= generalKeyPairRepository.deletekeypair(Id);

        return "keys deleted sucessfully";
    }
    @GetMapping("/Genfindall")
    public List<Object> findGenall()
    {
        return generalKeyPairRepository.findall();
    }

    @GetMapping("/home1")
    public String home1(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String userId = oauth2User.getAttribute("preferred_username");
            if (userId != null) {
                return "User ID: " + userId;
            } else {
                return "User ID not available";
            }
        } else {
            return "User ID not available";
        }
    }

    @GetMapping("/userinfo")
    public String getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            String userId = oidcUser.getName(); // Or any other attribute that contains the user ID
            return "User ID: " + userId;
        } else {
            return "User not authenticated";
        }
    }
    @GetMapping("/userName")
    public String userName(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String userId = oauth2User.getAttribute("preferred_username");
            if (userId != null) {
                return "User ID: " + userId;
            } else {
                return "User ID not available";
            }
        } else {
            return "User ID not available";
        }
    }
    @GetMapping("/userId")
    public String getuserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            String userId = oidcUser.getName(); // Or any other attribute that contains the user ID
            return "User ID: " + userId;
        } else {
            return "User not authenticated";
        }
    }

    @GetMapping("/OwnerID")
    public String Owner_ID(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String ownerId = (String) oauth2User.getAttribute("Owner_ID");
            if (ownerId != null) {
                return "Owner ID: " + ownerId;
            } else {
                return "Owner ID not available";
            }
        } else {
            return "User ID not available";
        }
    }




}

//
//
//import com.productcnit.Service.KeyManager;
//import com.productcnit.dto.KeyPairResponse;
//import com.productcnit.dto.PublicKeyMessage;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/key")
//public class KeyexchangeController {
//
//
//    private String privateKey2;
//    private String publicKey2;
//
//
//    private final HttpSession session;
//    private final KafkaTemplate<String, PublicKeyMessage> kafkaTemplate;
//
//
//    @Autowired
//    private final KeyManager keyManager3;
//
//    public KeyexchangeController(HttpSession session, KafkaTemplate<String, PublicKeyMessage> kafkaTemplate, KeyManager keyManager) {
//        this.session = session;
//        this.kafkaTemplate = kafkaTemplate;
//        this.keyManager3 = keyManager;
//    }
//
//    @GetMapping("/getkeypair")
//    public KeyPairResponse getpairkey() {
//        KeyManager.generateKeyPair();
//        String prvkey = keyManager3.generatePrviatekey();
//        String pubkey = keyManager3.generatePublicKey();
//        return new KeyPairResponse(prvkey, pubkey);
//    }
//
//    @GetMapping("/getpair")
//    public KeyPairResponse getKeyStrPrv()
//    {
//        try {
//            WebClient webClient = WebClient.create("http://localhost:8088/key");
//
//            // Build the request
//            WebClient.RequestHeadersSpec<?> requestSpec = webClient
//                    .method(HttpMethod.GET)
//                    .uri("/getkeypair");
//
//            // Execute the request
//            Mono<KeyPairResponse> responseMono = requestSpec.retrieve().bodyToMono(KeyPairResponse.class);
//
//            // Block and get the response
//            KeyPairResponse response = responseMono.block();
//            if (response != null) {
//                // Access the private key and public key
//                privateKey2 = response.getPrivatekey();
//                String publicKey1 = response.getPublickey();
//                session.setAttribute("privateKeyx", privateKey2);
//                session.setAttribute("publicKeyx", publicKey1);
//                System.out.println("Private Key: " + privateKey2);
//                System.out.println("Public Key: " + publicKey1);
//                return new KeyPairResponse(privateKey2,publicKey1);
//            } else {
//                return null;
//            }
//        } catch (Exception ignored) {
//            System.out.println("Error: " + ignored.getMessage());
//            return null;
//        }
//    }
//
//    @GetMapping("/getpub")
//    public ResponseEntity<String> getPublicKey() {
//        String publicKey = (String) session.getAttribute("publicKeyx");
//        if (publicKey != null) {
//            PublicKeyMessage publicKeyMessage = new PublicKeyMessage("client-idx", publicKey);
//            kafkaTemplate.send("public-key-topic", "client-idx", publicKeyMessage);
//            return ResponseEntity.ok("Public key published successfully");
//        } else {
//            return ResponseEntity.badRequest().body("Public key not found in session");
//        }
//    }
//
//    @GetMapping("/testsession")
//    public String testSession() {
//        session.setAttribute("testAttribute", "testValue");
//        String testValue = (String) session.getAttribute("testAttribute");
//        return "Test value from session: " + testValue;
//    }
//
//    @KafkaListener(topics = "public-key-topic", groupId = "group-id")
//    public String getsecsharedkey(PublicKeyMessage publicKeyMessage)
//   {
////       String privateKey = (String) session.getAttribute("privateKeyx");
//       System.out.println(privateKey2);
//       if (privateKey2 == null) {
//           return "Private key not found in session";
//       }
//       String PeerPubKey = publicKeyMessage.getPublicKey();
//
////       try
////       {
////           WebClient webClient = WebClient.create("http://localhost:8088/key");
////
////           // Build the request
////           WebClient.RequestHeadersSpec<?> requestSpec = webClient
////                   .method(HttpMethod.GET)
////                   .uri("/getpub");
////
////           // Execute the request
////           Mono<String> responseMono = requestSpec.retrieve().bodyToMono(String.class);
////
////           // Block and get the response
////           String response = responseMono.block();
////           PeerPubKey= response.toString();
////           System.out.println("this is the peerpublickey" + PeerPubKey);
////           System.out.println("this is the privatekey" + privateKey);
////       }
////       catch (Exception ignored)
////       {
////           System.out.println("error");
////       }
//
//       keyManager3.initFromStringsPublickey(PeerPubKey);
//       keyManager3.initFromStringsPrvkey(privateKey2);
//       String sharedKey = keyManager3.generateSharedSecret();
//       System.out.println("Shared key: " + sharedKey);
//       return sharedKey;
//   }
//}
