package com.epam.taskthree.controller;

import com.epam.taskthree.model.Patient;
import com.epam.taskthree.service.PatientService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllPatients() throws Exception {
        // Mock data
        Patient patient = new Patient();
        patient.setId(1L);
        List<Patient> patients = new ArrayList<>();
        patients.add(patient);
        when(patientService.getAllPatients()).thenReturn(patients);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetPatientById() throws Exception {
        // Mock data
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientService.getPatientById(1L)).thenReturn(patient);

        // Perform GET request with existing ID and verify response
        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        // Perform GET request with non-existing ID and verify response
        mockMvc.perform(get("/api/patients/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreatePatient() throws Exception {
        // Mock data
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        // Perform POST request with patient data and verify response
        mockMvc.perform(post("/api/patients")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdatePatient() throws Exception {
        // Mock data
        Patient patient = new Patient();
        patient.setName("name");
        patient.setId(1L);
        when(patientService.updatePatient(eq(1L), any())).thenReturn(patient);

        // Perform PUT request with patient data and verify response
        mockMvc.perform(put("/api/patients/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"name\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeletePatient() throws Exception {
        when(patientService.deletePatient(1L)).thenReturn(true);
        // Perform DELETE request with patient ID and verify response
        mockMvc.perform(delete("/api/patients/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Verify that service method is called with correct ID
        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetPatientByIdWhenPatientNotFound() throws Exception {
        // Mock data - Patient not found
        when(patientService.getPatientById(100L)).thenReturn(null);

        // Perform GET request with non-existing ID and verify response
        mockMvc.perform(get("/api/patients/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdatePatientWhenPatientNotFound() throws Exception {
        // Mock data - Patient not found
        when(patientService.updatePatient(100L, new Patient())).thenReturn(null);

        // Perform PUT request with non-existing ID and verify response
        mockMvc.perform(put("/api/patients/100")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeletePatientWhenPatientNotFound() throws Exception {
        when(patientService.deletePatient(100L)).thenReturn(false);
        // Perform DELETE request with non-existing ID and verify response
        mockMvc.perform(delete("/api/patients/100")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
