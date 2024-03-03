package com.epam.taskthree.service;

import com.epam.taskthree.model.Appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Long id);
    Appointment scheduleAppointment(Appointment appointment);
    void cancelAppointment(Long id);
}
