package com.MindMate.agents.wellness;

import com.MindMate.agents.wellness.Service.ChatService;
import com.MindMate.agents.escalation.RiskDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private RiskDetectionService riskDetectionService;

    @PostMapping("/getResponse")
    public ResponseEntity<Flux<String>> chat(@RequestParam("message") String message){

        return ResponseEntity.ok(chatService.chat(message));
    }

  //  @GetMapping("/loadHistory")


    @GetMapping("/loadHistory")
    public ResponseEntity<?> loadChatHistory(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(chatService.loadChatHistory(page));
    }
}
