package com.MindMate.agents.carejourney;

import com.MindMate.agents.assessment.Repo.AssessmentResultRepo;
import com.MindMate.agents.assessment.model.AssessmentResult;
import com.MindMate.agents.escalation.RiskDetectionService;
import com.MindMate.agents.untils.ChatHistoryService;
import com.MindMate.agents.untils.MemoryService;
import com.MindMate.model.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CareJourneyDataService {

    private final MemoryService memoryService;
    private final ChatHistoryService chatHistoryService;
    private final RiskDetectionService riskDetectionService;
    private final AssessmentResultRepo assessmentResultRepo;

    public PatientContextDto buildPatientContext(User user){

        String longTermSummary = memoryService.getSummary(user.getId());
        String recentChatHistory = chatHistoryService.getLast10Message(user).toString();
        String currentRiskStatus = Objects.requireNonNull(riskDetectionService.getCurrentRiskStatus(user)).getRiskLevel().name();
        List<AssessmentResult> recentAssessments = assessmentResultRepo.findLatestPerAssessmentType(user.getId()).orElse(null);

        Integer age = calculateAge(user.getDob());
        String name = user.getUsername();
        Long userId = user.getId();

        return new PatientContextDto(
                userId,
                name,
                age,
                longTermSummary,
                recentChatHistory,
                currentRiskStatus,
                recentAssessments);

    }

    private int calculateAge(Date dob) {
        if (dob == null) return 25;

        LocalDate birthDate = dob.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate currentDate = LocalDate.now();

        return Period.between(birthDate, currentDate).getYears();
    }

}
