package com.productcnit.controller;

import com.productcnit.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

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

    @MessageMapping("/api/socket/public_key_ca")
    @SendTo("/topic/public_key_ca")
    @CrossOrigin("*")
    public Peer_publickey_ca Send_publickey_ca(Peer_publickey_ca publicKeyMessage) throws Exception {
        Peer_publickey_ca outMessage = new Peer_publickey_ca();
        outMessage.setSenderId(publicKeyMessage.getSenderId());
        outMessage.setEnc_Sig_Pubkey(publicKeyMessage.getEnc_Sig_Pubkey());
        outMessage.setPubkey_ca_sig(publicKeyMessage.getPubkey_ca_sig());
        outMessage.setRecId(publicKeyMessage.getRecId());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        return outMessage;
    }

    @MessageMapping("/api/socket/public_key_ca_rec")
    @SendTo("/topic/public_key_ca_rec")
    @CrossOrigin("*")
    public Peer_publickey_ca Send_publickey_ca_rec(Peer_publickey_ca publicKeyMessage) throws Exception {
        Peer_publickey_ca outMessage = new Peer_publickey_ca();
        outMessage.setSenderId(publicKeyMessage.getSenderId());
        outMessage.setEnc_Sig_Pubkey(publicKeyMessage.getEnc_Sig_Pubkey());
        outMessage.setPubkey_ca_sig(publicKeyMessage.getPubkey_ca_sig());
        outMessage.setRecId(publicKeyMessage.getRecId());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        return outMessage;
    }

    @MessageMapping("/api/socket/peer-public-key")
    @SendTo("/topic/peer-public-key")
    @CrossOrigin("*")
    public PublicKeyMessage send_peer_publickey(PublicKeyMessage publicKeyMessage) throws Exception {
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
