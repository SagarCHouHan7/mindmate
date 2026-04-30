package com.MindMate.appointments.controller;

import com.MindMate.appointments.dto.AppointmentRequestDto;
import com.MindMate.appointments.dto.AppointmentResDtoForExpert;
import com.MindMate.appointments.dto.AppointmentResDtoForUser;
import com.MindMate.appointments.enums.AppointmentStatus;
import com.MindMate.appointments.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/appointment/user/request")
    public ResponseEntity<?> requestForAppointment(@RequestBody AppointmentRequestDto appointmentRequestDto){
        return new ResponseEntity<>(appointmentService.requestAppointment(appointmentRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/appointment/user/checkTimeSlot")
    public ResponseEntity<?> checkTimeSlot(@RequestBody AppointmentRequestDto appointmentRequest){
        return ResponseEntity.ok(appointmentService.checkSlotAvailability(appointmentRequest));
    }

    @PutMapping("/appointment/user/cancel/{id}")
    public ResponseEntity<?> user_cancelMyAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.user_cancelMyAppointment(id));
    }
    @GetMapping("/appointment/user/getAll")
    public ResponseEntity<List<AppointmentResDtoForUser>> getUsersAllAppointments(){
        return ResponseEntity.ok(appointmentService.getByUserId());
    }

    @GetMapping("/appointment/expert/getAll")
    public ResponseEntity<List<AppointmentResDtoForExpert>> getAppointmentsByExpertId(){
        return ResponseEntity.ok(appointmentService.getByExpertId());
    }

    @PutMapping("/appointment/expert/markStatus/{id}")
    public ResponseEntity<AppointmentResDtoForExpert> markStatus(@PathVariable long id, @RequestParam AppointmentStatus status){
        return new ResponseEntity<>(appointmentService.markStatus(id, status), HttpStatus.OK);
    }

}
