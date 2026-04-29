package com.MindMate.service.AIservices.AssesmentAgentServices;

import com.MindMate.repository.AIRepo.RiskStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentEligibilityService {

    @Autowired
    private RiskStatusRepo riskStatusRepo;

    public boolean checkEligibility(Long userId){

        return true;
    }
}
