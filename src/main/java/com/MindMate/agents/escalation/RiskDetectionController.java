package com.MindMate.agents.escalation;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/escalation")
@AllArgsConstructor
public class RiskDetectionController {

    private final RiskDetectionService riskDetectionService;
    @GetMapping("/getRiskStatus")
    public ResponseEntity<RiskStatus> getRiskStatus(){

        return ResponseEntity.ok(riskDetectionService.getCurrentRiskStatus());
    }
}
