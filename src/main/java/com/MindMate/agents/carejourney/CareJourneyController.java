package com.MindMate.agents.carejourney;

import com.MindMate.appointments.Appointment;
import com.MindMate.appointments.AppointmentRepo;
import com.MindMate.model.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/care-journey")
@RequiredArgsConstructor
public class CareJourneyController {
    private final PatientReportRepo patientReportRepo;
    private final AppointmentRepo appointmentRepo;
    private final CareJourneyAgentService careJourneyAgentService;


    @GetMapping("/report/{appointmentId}")
    public ResponseEntity<PatientReport> getPatientReport(@PathVariable Long appointmentId){

//        return ResponseEntity.ok(patientReportRepo.findByAppointmentId(appointmentId)
//                .orElse(new PatientReport()));

        Optional<PatientReport> report = patientReportRepo.findByAppointmentId(appointmentId);
        if(report.isPresent()){
            return ResponseEntity.ok(report.get());
        }else {
            generateReportIfNotExist(appointmentId);
            return ResponseEntity.ok(new PatientReport());
        }

    }

    public void generateReportIfNotExist(Long appointmentId){

        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(() -> new RuntimeException("appointment is not available"));

        User user = appointment.getUser();

        //async call
        careJourneyAgentService.generatePatientReportAndSaveInDB(user, appointment);

    }

}
