package com.MindMate.agents.wellness.Service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorStore vectorStore;

    public RagService(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }


    public String retrieveMemories(Long userId, String userMessage){

        SearchRequest request =
                SearchRequest.builder()
                        .query(userMessage)
                        .topK(5)
                        .filterExpression("userId==" + userId)
                        .build();


        try {
            List<Document> memories =
                    vectorStore.similaritySearch(request);


            if(memories.isEmpty()){
                return "";
            }


            return memories.stream()
                    .map(Document::getText).filter(Objects::nonNull)
                    .map(text -> text.length() > 500 ? text.substring(0, 500) : text)
                    .collect(Collectors.joining("\n"));

        }catch (Exception e){
            e.printStackTrace();
            return "";
        }



    }

}