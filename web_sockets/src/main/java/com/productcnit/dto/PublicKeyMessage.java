package com.productcnit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicKeyMessage {
    private String senderId;
    private String publicKey;
    private String recId;
    private String time;
}



