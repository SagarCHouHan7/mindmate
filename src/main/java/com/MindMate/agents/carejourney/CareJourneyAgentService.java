package com.MindMate.agents.carejourney;

import com.MindMate.appointments.Appointment;
import com.MindMate.appointments.AppointmentRepo;
import com.MindMate.model.Notification;
import com.MindMate.model.account.User;
import com.MindMate.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CareJourneyAgentService {

    private final CareJourneyDataService careJourneyDataService;
    private final ChatClient chatClient;
    private final PatientReportRepo reportRepo;
    private final NotificationService notificationService;
    private final AppointmentRepo appointmentRepo;

    @Value("classpath:/prompts/care-journey-report-template.st")
    private Resource reportTemplate;

    @Async("taskExecutor")
    public void generatePatientReportAndSaveInDB(User user, Appointment appointment ){
        System.out.println("inside report generation for appointment: " + appointment.getId() + " and user: " + user.getId());

        //avoid duplicate report generation for the same appointment
        if(reportRepo.findByAppointmentId(appointment.getId()).isPresent()){
            return;
        }

        PatientContextDto ctx = careJourneyDataService.buildPatientContext(user);

        StringBuilder assessmentResultBuilder = new StringBuilder();

        ctx.assessmentResults().forEach(result->{
            assessmentResultBuilder.append("assessmentType: ")
                    .append(result.getAssessmentType())
                    .append(", score: ")
                    .append(result.getTotalScore())
                    .append(", severity: ")
                    .append(result.getSeverity())
                    .append(", aiInterpretation: ")
                    .append(result.getAiInterpretation())
                    .append("\n");
        });

        System.out.println("generating report for user: " + ctx.name() + " with age: " + ctx.age());
        String response = chatClient.prompt()
                .user(u -> u.text(reportTemplate)
                        .param("name", ctx.name())
                        .param("age", ctx.age())
                        .param("longTerm", ctx.longTermSummary())
                        .param("shortTerm", ctx.shortTermSummary())
                        .param("risk", ctx.riskStatus())
                        .param("assessment", assessmentResultBuilder.toString())
                )
                .call()
                .content();

        System.out.println("genrated report: ");

        System.out.println(response);

        PatientReport entity = new PatientReport();
        entity.setUserId(user.getId());
        entity.setAppointmentId(appointment.getId());
        entity.setReportText(response);

        reportRepo.save(entity);

        Notification notification = new Notification();
        notification.setAccount(appointment.getExpert());
        notification.setNote("New patient report generated for appointment with " + ctx.name());
        notificationService.addNotification(notification);
    }



}
