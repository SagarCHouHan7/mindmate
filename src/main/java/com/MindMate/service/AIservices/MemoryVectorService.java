package com.MindMate.service.AIservices;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemoryVectorService {

    private final VectorStore vectorStore;

    public MemoryVectorService(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }


    public void saveMemory(
            Long userId,
            String memoryContent,
            String memoryType
    ){

        Document document = new Document(memoryContent, Map.of(
                                "userId",
                                userId.toString(),
                                "type",
                                memoryType
                        )
                );

        vectorStore.add(List.of(document));
    }

}