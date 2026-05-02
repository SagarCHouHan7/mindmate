package com.MindMate.agents.lifestyle;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lifestyle")
public class LifeStyleController {

    private final RoutineService routineService;


    @PostMapping("/generate")
    public ResponseEntity<Boolean> generateRoutine(@RequestBody UserRoutineInput input){

        return ResponseEntity.ok(true);
    }

    @GetMapping("/routine")
    public ResponseEntity<LifestyleRoutine> getRoutine(){
        return ResponseEntity.ok(routineService.getRoutine());
    }

}
