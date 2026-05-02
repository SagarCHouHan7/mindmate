package com.MindMate.agents.carejourney;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/care-journey")
@RequiredArgsConstructor
public class CareJourneyController {
    private final PatientReportRepo patientReportRepo;

    @GetMapping("/report/{appointmentId}")
    public ResponseEntity<PatientReport> getPatientReport(@PathVariable Long appointmentId){
        return ResponseEntity.ok(patientReportRepo.findByAppointmentId(appointmentId)
                .orElse(new PatientReport()));
    }
}
