package com.MindMate.agents.assessment.service;

import com.MindMate.agents.assessment.enums.AssessmentType;
import com.MindMate.agents.assessment.Repo.AssessmentQuestionRepo;
import com.MindMate.agents.assessment.Repo.AssessmentRepo;
import com.MindMate.agents.assessment.model.Assessment;
import com.MindMate.agents.assessment.model.AssessmentQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssessmentSeeder implements CommandLineRunner {

    private final AssessmentRepo assessmentRepository;
    private final AssessmentQuestionRepo questionRepository;

    @Override
    public void run(String... args) {

        seedStress();
        seedAnxiety();
        seedLoneliness();
    }

    private void seedStress(){

        if(assessmentRepository.findByType(
                AssessmentType.STRESS).isPresent()){
            return;
        }

        Assessment assessment = new Assessment();
        assessment.setType(AssessmentType.STRESS);
        assessment.setTitle("Stress Assessment");
        assessment.setDescription(
                "Measures perceived stress level"
        );

        assessment=assessmentRepository.save(assessment);

        saveQuestion(assessment,1,
                "I feel overwhelmed by my responsibilities");

        saveQuestion(assessment,2,
                "I find it difficult to relax");

        saveQuestion(assessment,3,
                "I feel mentally exhausted");

        saveQuestion(assessment,4,
                "Stress affects my concentration");

        saveQuestion(assessment,5,
                "I feel pressure I struggle to manage");
    }

    private void seedAnxiety(){

        if(assessmentRepository.findByType(
                AssessmentType.ANXIETY).isPresent()){
            return;
        }

        Assessment assessment = new Assessment();
        assessment.setType(AssessmentType.ANXIETY);
        assessment.setTitle("Anxiety Assessment");
        assessment.setDescription(
                "Measures anxiety tendencies"
        );

        assessment=assessmentRepository.save(assessment);

        saveQuestion(assessment,1,
                "I feel nervous or on edge");

        saveQuestion(assessment,2,
                "I struggle to control worrying");

        saveQuestion(assessment,3,
                "I overthink negative outcomes");

        saveQuestion(assessment,4,
                "I feel restless or unable to stay calm");

        saveQuestion(assessment,5,
                "Worry interferes with daily activities");
    }

    private void seedLoneliness(){

        if(assessmentRepository.findByType(
                AssessmentType.LONELINESS).isPresent()){
            return;
        }

        Assessment assessment = new Assessment();
        assessment.setType(AssessmentType.LONELINESS);
        assessment.setTitle("Loneliness Assessment");
        assessment.setDescription(
                "Measures social disconnection"
        );

        assessment=assessmentRepository.save(assessment);

        saveQuestion(assessment,1,
                "I feel isolated from others");

        saveQuestion(assessment,2,
                "I feel I lack meaningful connection");

        saveQuestion(assessment,3,
                "I often feel alone even around people");

        saveQuestion(assessment,4,
                "I hesitate to reach out for support");

        saveQuestion(assessment,5,
                "I feel emotionally disconnected");
    }

    private void saveQuestion(
            Assessment assessment,
            int order,
            String text){

        AssessmentQuestion q=
                new AssessmentQuestion();

        q.setAssessment(assessment);
        q.setQuestionOrder(order);
        q.setQuestion(text);
        q.setWeight(1);

        questionRepository.save(q);
    }
}