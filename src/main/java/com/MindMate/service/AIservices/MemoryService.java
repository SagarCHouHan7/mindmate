package com.MindMate.service.AIservices;

import com.MindMate.model.UserMemory;
import com.MindMate.repository.AIRepo.UserMemoryRepo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class MemoryService {

    @Autowired
    private UserMemoryRepo memoryRepo;

    @Autowired
    private ChatClient chatClient;

    @Value("classpath:/prompts/AIWellnessExpert/user-memory-update-template.st")
    Resource memoryUpdateTemplate;


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

        UserMemory memory =
                memoryRepo.findById(userId)
                        .orElse(new UserMemory());

        memory.setUserId(userId);
        memory.setSummary(updatedSummary);

        memoryRepo.save(memory);
    }
}
