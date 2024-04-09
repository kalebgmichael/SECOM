package com.productcnit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("EncKeyRepository")
public class EncKey implements Serializable {
    @Id
    private String Owner_Id;
    private String Pair_Id;
    private String Enc_Key;
}
