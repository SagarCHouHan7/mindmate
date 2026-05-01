package com.MindMate.agents.quickchat;

import com.MindMate.model.account.User;
import com.MindMate.service.Utils.CurrentRoleService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class QuickChatService {

    @Autowired
    private CurrentRoleService currentRoleService;

    private final ChatClient chatClient;
    private static final int MAX_HISTORY = 15;
    private final Map<String, List<QuickChatMessage>> conversations = new ConcurrentHashMap<>();

    @Value("classpath:/prompts/quick-chat-system-guide.st")
    Resource quickChatTemplate;

    public QuickChatService(ChatClient chatClient){
        this.chatClient = chatClient;
    }


    public Flux<String> getNextResponse(String messages) {

        System.out.println(messages);
        User user = currentRoleService.getCurrentUser();
        return chatClient.prompt()
                .user(u->u.text(quickChatTemplate)
                        .param("username" , user.getUsername())
                        .param("age", calculateAge(user.getDob()))
                        .param("conversations", messages))
                .stream()
                .content();
    }

    public Boolean reset() {
        conversations.remove(currentRoleService.getCurrentUser().getId()+"_quick");
        return true;
    }

    private int calculateAge(Date dob) {
        if (dob == null) return 25;

        LocalDate birthDate = dob.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate currentDate = LocalDate.now();

        return Period.between(birthDate, currentDate).getYears();
    }

}
