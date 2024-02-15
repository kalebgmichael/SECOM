package com.productcnit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenKeyPairResponse implements Serializable {

    private String Gen_Owner_Id;
    private String Gen_User_Id;
    private String Gen_public_Key;
    private String Gen_private_Key;


}
