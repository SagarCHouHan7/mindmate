package com.MindMate.agents.quickchat;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class QuickChatController {


    private final QuickChatService quickChatService;

    @PostMapping("/quickChat")
    public ResponseEntity<Flux<String>> getResponse(@RequestBody String message){
        return ResponseEntity.ok(quickChatService.getNextResponse(message));
    }

    @DeleteMapping("/quickChat/reset")
    public ResponseEntity<Boolean> reset() {
        return ResponseEntity.ok(quickChatService.reset());
    }
}
