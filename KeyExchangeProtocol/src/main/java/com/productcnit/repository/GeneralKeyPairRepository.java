package com.productcnit.repository;

import com.productcnit.dto.GenKeyPairResponse;
import com.productcnit.model.KeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class GeneralKeyPairRepository {
    private static final String HASH_KEY = "key-pair";

    private final RedisTemplate<String, GenKeyPairResponse> generalKeyPairRedisTemplate;

    @Autowired
    public GeneralKeyPairRepository(RedisTemplate<String, GenKeyPairResponse> generalKeyPairRedisTemplate) {
        this.generalKeyPairRedisTemplate = generalKeyPairRedisTemplate;
    }

    public GenKeyPairResponse save(GenKeyPairResponse genKeyPairResponse) {
        generalKeyPairRedisTemplate.opsForHash().put(HASH_KEY, genKeyPairResponse.getGen_Owner_Id(), genKeyPairResponse);
        return genKeyPairResponse;
    }

    public GenKeyPairResponse findKeypairbyId(String Owner_Id) {
        return (GenKeyPairResponse) generalKeyPairRedisTemplate.opsForHash().get(HASH_KEY, Owner_Id);
    }

    public List<Object> findall()
    {
        return generalKeyPairRedisTemplate.opsForHash().values(HASH_KEY);
    }

    public String deletekeypair(String Owner_Id) {
        generalKeyPairRedisTemplate.opsForHash().delete(HASH_KEY, Owner_Id);
        return "KeyPair removed";
    }

}
