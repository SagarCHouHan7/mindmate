package com.MindMate.agents.moderation;

import com.MindMate.community.model.Answer;
import com.MindMate.community.model.Question;
import com.MindMate.community.repo.AnswerRepo;
import com.MindMate.community.repo.QuestionRepo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AIResponseService {
    @Autowired
    private AnswerRepo answerRepo;
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private QuestionRepo questionRepo;

    @Value("classpath:prompts/system-message.st")
    private Resource systemMessage;

    @Async("taskExecutor")
    public void getAIAnswerAndSaveInDB(Question question){
        System.out.println("entered");
        System.out.println(question);

            String response = chatClient.prompt()
                    .system(s->s.text(systemMessage))
                    .user(u -> u.text(question.getQuestion()))
                    .call()
                    .content();
            saveAIAnswer(question, response);
        System.out.println("out");

    }
    @Transactional
    public void saveAIAnswer(Question question, String response){

        Answer answer = new Answer();
        answer.setAnsweredBy(null);
        answer.setAnswer(response);
        answer.setQuestion(question);
        answer.setCreatedAt(LocalDateTime.now());
        answer.setUpdatedAt(LocalDateTime.now());
        answer.setLikes(0);

        Answer save = answerRepo.save(answer);
        question.setAnswerCount(question.getAnswerCount()+1);
        questionRepo.save(question);
    }
}

