package com.productcnit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncKeyResponse implements Serializable {
    private String Owner_Id;
    private String Pair_Id;
    private String Enc_Key;
}
