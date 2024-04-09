package com.productcnit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Own_PublicKeyMessageSend {
    private String senderId;
    private String Enc_Sig_Own_Pubkey;
    private String recId;
}
