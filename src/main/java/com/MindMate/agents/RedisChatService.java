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

        redisTemplate.opsForList().rightPush(key, value);

        // keep only last 10
        redisTemplate.opsForList().trim(key, -10, -1);

        // optional TTL
        redisTemplate.expire(key, Duration.ofHours(1));
    }

    public List<String> getLastMessages(Long userId) {
        String key = getKey(userId);
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
