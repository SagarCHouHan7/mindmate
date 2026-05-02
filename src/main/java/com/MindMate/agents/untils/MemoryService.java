package com.MindMate.agents.untils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemoryService {

    private final UserMemoryRepo memoryRepo;
    private final ChatClient chatClient;

    @Value("classpath:/prompts/AIWellnessExpert/user-memory-update-template.st")
    Resource memoryUpdateTemplate;

    // Retrieves the long-term summary for a user, or returns an empty string if not found
    public String getSummary(Long userId){
        return memoryRepo.findById(userId)
                .map(UserMemory::getSummary)
                .orElse("");
    }


    public void updateSummary(
            Long userId,
            String latestUserMsg,
            String aiReply
    ){

        String oldSummary = getSummary(userId);

        String updatedSummary =
                chatClient.prompt()
                        .user(u->u.text(memoryUpdateTemplate)
                                .param("oldSummary", oldSummary)
                                .param("msg", latestUserMsg)
                                .param("reply", aiReply)
                        )
                        .call()
                        .content();

        UserMemory memory = memoryRepo.findById(userId).orElse(new UserMemory());

        memory.setUserId(userId);
        memory.setSummary(updatedSummary);

        memoryRepo.save(memory);
    }
}
