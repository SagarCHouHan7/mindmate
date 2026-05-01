package com.MindMate.agents.assessment.service;

import com.MindMate.agents.escalation.RiskStatus;
import com.MindMate.model.account.User;
import com.MindMate.agents.escalation.RiskStatusLevel;
import com.MindMate.agents.escalation.RiskDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssessmentEligibilityService {

    private final RiskDetectionService riskDetectionService;

    public boolean isEligible(User user){
        RiskStatus state = riskDetectionService.getCurrentRiskStatus(user);
        if(state == null) return false;
        return state.getRiskLevel() == RiskStatusLevel.HIGH
                || state.getRiskLevel() == RiskStatusLevel.MODERATE || state.getRiskLevel() == RiskStatusLevel.SEVERE;
    }


}
