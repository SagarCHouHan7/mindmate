package com.MindMate.controller.AIController;

import com.MindMate.model.RiskStatus;
import com.MindMate.service.AIservices.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/getResponse")
    public ResponseEntity<Flux<String>> chat(@RequestBody() String message){

        return ResponseEntity.ok(chatService.chat(message));
    }

  //  @GetMapping("/loadHistory")

    @GetMapping("/getRiskStatus")
    public ResponseEntity<RiskStatus> getRiskStatus(){
        return ResponseEntity.ok(chatService.getRiskStatus());
    }

}
