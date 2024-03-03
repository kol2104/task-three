package com.epam.taskthree.service.impl;

import com.epam.taskthree.model.Appointment;
import com.epam.taskthree.repository.AppointmentRepository;
import com.epam.taskthree.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public Appointment scheduleAppointment(Appointment appointment) {
        // Check for overlapping appointments
        if (isAppointmentOverlapping(appointment)) {
            throw new IllegalArgumentException("Appointment time overlaps with existing appointment.");
        }
        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private boolean isAppointmentOverlapping(Appointment newAppointment) {
        List<Appointment> existingAppointments = appointmentRepository.findByPatientId(newAppointment.getPatient().getId());
        for (Appointment existingAppointment : existingAppointments) {
            LocalDateTime existingStartTime = existingAppointment.getStartTime();
            LocalDateTime existingEndTime = existingAppointment.getEndTime(); // Assuming each appointment lasts 30 minutes
            LocalDateTime newStartTime = newAppointment.getStartTime();
            LocalDateTime newEndTime = newAppointment.getEndTime(); // Assuming each appointment lasts 30 minutes

            // Check for overlap
            if (newStartTime.isBefore(existingEndTime) && existingStartTime.isBefore(newEndTime)) {
                return true; // Overlapping appointments found
            }
        }
        return false; // No overlapping appointments found
    }
}