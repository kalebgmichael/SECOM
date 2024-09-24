package com.productcnit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peer_publickey_ca {
    private String senderId;
    private String Enc_Sig_Pubkey;
    private String Pubkey_ca_sig;
    private String recId;
    private String time;
}
