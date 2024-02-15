package com.productcnit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@RedisHash("GeneralKeyPairRepository")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralKeyPairRepository implements Serializable {
    @Id
    private String Owner_Id;
    private String User_Id;
    private String public_Key;
    private String private_Key;
}
