package com.productcnit.repository;


import com.productcnit.model.KeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeyPairRespository {

    private static final String HASH_KEY = "key-pair";

    private final RedisTemplate<String, KeyPair> keyPairRedisTemplate;

    @Autowired
    public KeyPairRespository(RedisTemplate<String, KeyPair> keyPairRedisTemplate) {
        this.keyPairRedisTemplate = keyPairRedisTemplate;
    }

    public KeyPair save(KeyPair keyPair) {
        keyPairRedisTemplate.opsForHash().put(HASH_KEY, keyPair.getRecId(), keyPair);
        return keyPair;
    }

    public KeyPair findKeypairbyId(String recId) {
        return (KeyPair) keyPairRedisTemplate.opsForHash().get(HASH_KEY, recId);
    }

    public List<Object> findall()
    {
        return keyPairRedisTemplate.opsForHash().values(HASH_KEY);
    }

    public String deletekeypair(String Id) {
        keyPairRedisTemplate.opsForHash().delete(HASH_KEY, Id);
        return "KeyPair removed";
    }



}
