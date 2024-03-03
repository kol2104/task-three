package com.epam.taskthree.controller;

import com.epam.taskthree.model.Appointment;
import com.epam.taskthree.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllAppointments() throws Exception {
        // Mock data
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        when(appointmentService.getAllAppointments()).thenReturn(appointments);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAppointmentById() throws Exception {
        // Mock data
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);

        // Perform GET request with existing ID and verify response
        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        // Perform GET request with non-existing ID and verify response
        mockMvc.perform(get("/api/appointments/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testScheduleAppointment() throws Exception {
        // Mock data
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        when(appointmentService.scheduleAppointment(any(Appointment.class))).thenReturn(appointment);

        // Perform POST request with appointment data and verify response
        mockMvc.perform(post("/api/appointments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    void testCancelAppointment() throws Exception {
        // Perform DELETE request with appointment ID and verify response
        mockMvc.perform(delete("/api/appointments/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Verify that service method is called with correct ID
        verify(appointmentService, times(1)).cancelAppointment(1L);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAppointmentByIdWithNullAppointment() throws Exception {
        // Mock data
        when(appointmentService.getAppointmentById(anyLong())).thenReturn(null);

        // Perform GET request with non-existing ID and verify response
        mockMvc.perform(get("/api/appointments/100"))
                .andExpect(status().isNotFound());
    }
}
