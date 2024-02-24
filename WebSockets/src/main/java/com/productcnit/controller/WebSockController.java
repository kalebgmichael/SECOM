package com.productcnit.controller;

import com.productcnit.dto.ChatMessage;
import com.productcnit.dto.Greeting;
import com.productcnit.dto.HelloMessage;
import com.productcnit.dto.MessageToSend;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

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

    @MessageMapping("/api/socket/chat")
    @SendTo("/topic/chat")
    @CrossOrigin("*")
    public MessageToSend send(ChatMessage message) throws Exception {
        MessageToSend outMessage = new MessageToSend();
        outMessage.setSender(message.getSender());
        outMessage.setText(message.getText());
        outMessage.setTime(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date()));
        return outMessage;
    }

}
