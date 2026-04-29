package com.MindMate.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.stereotype.Component;

@Component
public class QdrantInitializer {

    private final QdrantVectorStore vectorStore;

    public QdrantInitializer(
            QdrantVectorStore vectorStore
    ){
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() throws Exception {

        vectorStore.afterPropertiesSet();

    }
}