package com.MindMate.agents.untils;

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

    public StringBuilder getLast10Message(User user){
        List<ChatMessage> history = chatRepo.findTop10ByUserIdOrderByCreatedAtDesc(user.getId());
        Collections.reverse(history);
        StringBuilder pastMessages = new StringBuilder();

        for (ChatMessage msg : history) {
            pastMessages.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }
        return pastMessages;

    }

}
