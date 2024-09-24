package com.productcnit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peer_PublicKeyMessageSend {
    private String senderId;
    private String dh_Pubkey;
    private String ca_Pubkey;
    private String recId;
}
