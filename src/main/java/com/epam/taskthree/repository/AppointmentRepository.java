package com.epam.taskthree.repository;

import com.epam.taskthree.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment>  findByPatientId(Long patientId);
}
