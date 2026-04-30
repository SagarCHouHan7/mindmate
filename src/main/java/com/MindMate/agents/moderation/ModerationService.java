package com.MindMate.agents.moderation;

import com.MindMate.community.model.Answer;
import com.MindMate.model.Notification;
import com.MindMate.community.model.Question;
import com.MindMate.model.account.Account;
import com.MindMate.model.enums.NotificationStatus;
import com.MindMate.repository.AccountRepo;
import com.MindMate.community.repo.AnswerRepo;
import com.MindMate.community.repo.QuestionRepo;
import com.MindMate.service.NotificationService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
public class ModerationService {


    private final ChatClient chatClient;
    private final AnswerRepo answerRepo;
    private final QuestionRepo questionRepo;
    private final AccountRepo accountRepo;
    private final NotificationService notificationService;

    @Value("classpath:/prompts/community-support-policy-system-check.st")
    private Resource systemMessage;

    public ModerationService(ChatClient chatClient, AnswerRepo answerRepo, QuestionRepo questionRepo, AccountRepo accountRepo, NotificationService notificationService) {
        this.chatClient = chatClient;
        this.answerRepo = answerRepo;
        this.questionRepo = questionRepo;
        this.accountRepo = accountRepo;
        this.notificationService = notificationService;
    }

    @Async("taskExecutor")
    public void checkViolationPolicy(Answer answer, Question question, Account account){

        System.out.println("async execution");
        try{
            String response = chatClient.prompt()
                    .user(u -> u.text(systemMessage)
                            .param("question", question.getQuestion())
                            .param("answer", answer.getAnswer()))
                    .call()
                    .content();

            System.out.println(response);
            String json = extractJson(response);
            ObjectMapper mapper = new ObjectMapper();
            ModerationResult result = mapper.readValue(json, ModerationResult.class);


            // Save moderation metadata
            assert result != null;
            answer.setSafetyRating(result.getRating());
            answer.setModerationSafe(result.isSafe());
            answer.setModerationReason(result.getReason());
            answer.setModerationCategories(
                    result.getCategories() == null ? "" : String.join(",", result.getCategories())
            );
//            answer.setSuggestedAnswer(result.getSuggestedAnswer());

            if (result.isSafe()) {
                answerRepo.save(answer);
                addNotification("Your response sent successfully", account);
                return;
            }
//            answerRepo.deleteById(answer.getId());
//            addNotification("Your response is deleted because it was violating the app policies", account);
            if (result.getRating() >= 6) {
                answerRepo.save(answer);
                addNotification("Your response sent successfully", account);
            } else if (result.getRating() >= 3) {
                answer.setSuggestedAnswer(result.getSuggestedAnswer());
                answer.setAnswer(result.getSuggestedAnswer());
                answer.setAnswerChanged(true);
                answer.setSuggestedAnswerSafetyRating(result.getSuggestedAnswerSafetyRating());
                answer.setModerationSafe(false);
                answer.setSafetyRating(result.getRating());
                answerRepo.save(answer); // or save in moderation table
                addNotification("Your response needs revision. We suggested a safer version.", account);
            } else {
                answerRepo.deleteById(answer.getId());
                addNotification("Your response was removed because it violated the app policies", account);
                question.setAnswerCount(question.getAnswerCount() - 1);
                questionRepo.save(question);
            }
            question.setAnswerCount(question.getAnswerCount()-1);
            questionRepo.save(question);


        }catch (Exception e){
            e.printStackTrace();
            answerRepo.deleteById(answer.getId());
            question.setAnswerCount(question.getAnswerCount() - 1);
            questionRepo.save(question);
        }


    }

    private void addNotification(String note, Account account){
        Notification notification = new Notification();
        notification.setAccount(account);
        notification.setTime(LocalDateTime.now());
        notification.setNote(note);
        notification.setStatus(NotificationStatus.UNREAD);
        notificationService.addNotification(notification);
    }

    private String extractJson(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("AI response is empty");
        }

        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');

        if (start == -1 || end == -1 || end < start) {
            throw new IllegalArgumentException("No valid JSON object found in AI response: " + raw);
        }

        return raw.substring(start, end + 1);
    }
}
