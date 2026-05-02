package com.MindMate.agents.carejourney;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientReportRepo extends JpaRepository<PatientReport, Long> {

    Optional<PatientReport> findByAppointmentId(Long appointmentId);
}
