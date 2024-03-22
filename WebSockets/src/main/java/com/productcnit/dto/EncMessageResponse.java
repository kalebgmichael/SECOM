package com.productcnit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncMessageResponse {
    String SenderId;
    String RecId;
    String message;
    String time;

}
