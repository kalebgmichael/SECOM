package com.productcnit.repository;

import com.productcnit.dto.EncKeyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<EncKeyResponse> findAll1() {
        List<Object> allValues = EnckeyRedisTemplate.opsForHash().values(HASH_KEY);
        return allValues.stream()
                .map(obj -> (EncKeyResponse) obj)
                .collect(Collectors.toList());
    }

    public List<EncKeyResponse> findByOwnerAndPairId(String Owner_Id, String Pair_Id) {
        // This method assumes that you have stored EncKey objects with Owner_Id as the hash key.
        // implement custom logic to filter by both Owner_Id and Pair_Id.
        // Since Redis does not natively support querying by multiple fields in a hash, you may need to retrieve all EncKey objects for the given Owner_Id and then filter them in Java.
        List<EncKeyResponse> allKeys = findAll1();
        return allKeys.stream()
                .filter(encKey -> encKey.getOwner_Id().equals(Owner_Id) && encKey.getPair_Id().equals(Pair_Id))
                .collect(Collectors.toList());
    }

    public String deletekeypair(String Owner_Id) {
        EnckeyRedisTemplate.opsForHash().delete(HASH_KEY, Owner_Id);
        return "KeyPair removed";
    }
}
