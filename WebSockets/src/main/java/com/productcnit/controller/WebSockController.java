package com.productcnit.controller;

import com.productcnit.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
//@RequestMapping("/api/socket")
public class WebSockController {

//    @GetMapping("/home")
//    public String home()
//    {
//        return "example";
//    }

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/api/socket/chat")
    @SendTo("/topic/chat")
    @CrossOrigin("*")
    public MessageToSend send(ChatMessage message) throws Exception {
        MessageToSend outMessage = new MessageToSend();
        System.out.println(message);
        outMessage.setRec(message.getRec());
        outMessage.setSender(message.getSender());
        outMessage.setText(message.getText());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        return outMessage;
    }

    @MessageMapping("/api/socket/public-key")
    @SendTo("/topic/public-key")
    @CrossOrigin("*")
    public PublicKeyMessage send_publickey(PublicKeyMessage publicKeyMessage) throws Exception {
        PublicKeyMessage outMessage = new PublicKeyMessage();
        outMessage.setPublicKey(publicKeyMessage.getPublicKey());
        outMessage.setSenderId(publicKeyMessage.getSenderId());
        outMessage.setRecId(publicKeyMessage.getRecId());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        return outMessage;
    }

    @MessageMapping("/api/socket/EncryptedMessage")
    @SendTo("/topic/EncryptedMessage")
    @CrossOrigin("*")
    public EncMessageResponse send_EncMessage(EncMessageResponse encMessage) throws Exception {
        EncMessageResponse outMessage = new EncMessageResponse();
        outMessage.setMessage(encMessage.getMessage());
        outMessage.setRecId(encMessage.getRecId());
        outMessage.setSenderId(encMessage.getSenderId());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        return outMessage;
    }

    @MessageMapping("/api/socket/private-chat")
    @CrossOrigin("*")
    public void send_private(@Payload ChatMessage message) throws Exception {
        System.out.println(message);
        simpMessagingTemplate.convertAndSendToUser(message.getRec(),"/specific/private-chat",message);
    }


}
