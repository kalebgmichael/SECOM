package com.productcnit.repository;

import com.productcnit.dto.EncKeyResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EncKeyRepository {
    private static final String HASH_KEY = "EncKey-pair";

    private final RedisTemplate<String, EncKeyResponse> EnckeyRedisTemplate;

    @Autowired
    public EncKeyRepository(RedisTemplate<String, EncKeyResponse> enckeyRedisTemplate) {
        EnckeyRedisTemplate = enckeyRedisTemplate;
    }
    public EncKeyResponse save(EncKeyResponse encKey) {
        EnckeyRedisTemplate.opsForHash().put(HASH_KEY, encKey.getOwner_Id(), encKey);
        return encKey;
    }

    public EncKeyResponse findKeypairbyId(String Owner_Id) {
        return (EncKeyResponse) EnckeyRedisTemplate.opsForHash().get(HASH_KEY, Owner_Id);
    }

    public List<Object> findall()
    {
        return EnckeyRedisTemplate.opsForHash().values(HASH_KEY);
    }

    public String deletekeypair(String Owner_Id) {
        EnckeyRedisTemplate.opsForHash().delete(HASH_KEY, Owner_Id);
        return "KeyPair removed";
    }
}
