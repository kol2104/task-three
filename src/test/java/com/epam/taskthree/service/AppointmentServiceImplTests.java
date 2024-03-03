package com.epam.taskthree.service;

import com.epam.taskthree.model.Appointment;
import com.epam.taskthree.model.Patient;
import com.epam.taskthree.repository.AppointmentRepository;
import com.epam.taskthree.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void testGetAllAppointments() {
        // Mock data
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // Test
        List<Appointment> result = appointmentService.getAllAppointments();

        // Verify
        assertEquals(1, result.size());
    }

    @Test
    void testGetAppointmentById() {
        // Mock data
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        // Test
        Appointment result = appointmentService.getAppointmentById(1L);

        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAppointmentByIdWhenNotFound() {
        // Mock data - Appointment not found
        when(appointmentRepository.findById(100L)).thenReturn(Optional.empty());

        // Test
        Appointment result = appointmentService.getAppointmentById(100L);

        // Verify
        assertNull(result);
    }

    @Test
    void testScheduleAppointment() {
        Patient patient = new Patient();
        patient.setId(1L);
        // Mock data
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentRepository.findByPatientId(anyLong())).thenReturn(new ArrayList<>());

        // Test
        Appointment result = appointmentService.scheduleAppointment(appointment);

        // Verify
        assertNotNull(result);
    }

    @Test
    void testScheduleAppointmentWithOverlap() {
        // Mock data - Existing appointment
        Appointment existingAppointment = new Appointment();
        existingAppointment.setStartTime(LocalDateTime.of(2024, 3, 1, 9, 0)); // Existing appointment from 9:00 AM to 9:30 AM
        existingAppointment.setEndTime(LocalDateTime.of(2024, 3, 1, 9, 30));

        Patient patient = new Patient();
        patient.setId(1L);
        // New appointment overlaps with existing appointment
        Appointment newAppointment = new Appointment();
        newAppointment.setStartTime(LocalDateTime.of(2024, 3, 1, 9, 15)); // New appointment from 9:15 AM to 9:45 AM
        newAppointment.setEndTime(LocalDateTime.of(2024, 3, 1, 9, 45));
        newAppointment.setPatient(patient);

        List<Appointment> existingAppointments = new ArrayList<>();
        existingAppointments.add(existingAppointment);
        when(appointmentRepository.findByPatientId(anyLong())).thenReturn(existingAppointments);

        // Test & Verify
        assertThrows(IllegalArgumentException.class, () -> appointmentService.scheduleAppointment(newAppointment));
    }

    @Test
    void testCancelAppointment() {
        // Test
        assertDoesNotThrow(() -> appointmentService.cancelAppointment(1L));

        // Verify
        verify(appointmentRepository, times(1)).deleteById(1L);
    }
}
