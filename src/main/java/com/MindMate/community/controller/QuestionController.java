package com.MindMate.community.controller;

import com.MindMate.community.service.QuestionService;
import com.MindMate.community.dto.CreateQuestionDto;
import com.MindMate.community.dto.QuestionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/post")
    private ResponseEntity<QuestionResponseDto> createQuestion(@RequestBody CreateQuestionDto question){
        QuestionResponseDto question1 = questionService.createQuestion(question);
        return new ResponseEntity<>(question1 , HttpStatus.CREATED);
    }

    @GetMapping("/getMy")
    private ResponseEntity<?> getMyQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return ResponseEntity.ok(questionService.getMyQuestions(page , size));
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<QuestionResponseDto> updateQuestionById(@PathVariable("id") long id , @RequestBody CreateQuestionDto question){
        return ResponseEntity.ok(questionService.updateQuestionById(id , question.getQuestion()));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Long id){

        return ResponseEntity.ok(questionService.deleteById(id));
    }
}
