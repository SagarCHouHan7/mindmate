package com.MindMate.agents;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisChatService {

    private final RedisTemplate<String, String> redisTemplate;

    private String getKey(Long userId) {
        return "chat:" + userId;
    }

    public void saveMessage(Long userId, String role, String content) {
        String key = getKey(userId);

        String value = role + ": " + content;

        try {
            redisTemplate.opsForList().rightPush(key, value);

            // keep only last 10
            redisTemplate.opsForList().trim(key, -10, -1);

            // optional TTL
            // redisTemplate.expire(key, Duration.ofHours(1));
            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                redisTemplate.expire(key, Duration.ofHours(1));
            }
        } catch (Exception e) {
            // log error
            System.out.println("Error saving message to Redis: " + e.getMessage());
        }
    }

    public List<String> getLastMessages(Long userId) {
        String key = getKey(userId);
        List<String> list = redisTemplate.opsForList().range(key, 0, -1);
        return list != null ? list : List.of();
        //return redisTemplate.opsForList().range(key, 0, -1);
    }
}
