package com.MindMate.agents.untils;

import com.MindMate.agents.RedisChatService;
import com.MindMate.agents.wellness.ChatMessage;
import com.MindMate.agents.wellness.ChatRepo;
import com.MindMate.model.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {

    private final ChatRepo chatRepo;
    private final RedisChatService redisChatService;

    public StringBuilder getLast10Message(User user){
        List<ChatMessage> history = chatRepo.findTop10ByUserIdOrderByCreatedAtDesc(user.getId());
        Collections.reverse(history);
        StringBuilder pastMessages = new StringBuilder();

        for (ChatMessage msg : history) {
            pastMessages.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }
        return pastMessages;

    }

    public StringBuilder getLast10MessageFromRedis(User user) {

        StringBuilder pastMessages = new StringBuilder();

        try {
            List<String> cached = redisChatService.getLastMessages(user.getId());

            if (cached != null && !cached.isEmpty()) {
                for (String msg : cached) {
                    pastMessages.append(msg).append("\n");
                }
                return pastMessages;
            }

        } catch (Exception e) {
            System.out.println("⚠️ Redis failed, falling back to DB");
        }

        // ✅ fallback to DB
        List<ChatMessage> history =
                chatRepo.findTop10ByUserIdOrderByCreatedAtDesc(user.getId());

        Collections.reverse(history);

        for (ChatMessage msg : history) {
            pastMessages.append(msg.getRole())
                    .append(": ")
                    .append(msg.getContent())
                    .append("\n");
        }

        return pastMessages;
    }
}
