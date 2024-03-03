package com.epam.taskthree.controller;

import com.epam.taskthree.model.Prescription;
import com.epam.taskthree.service.PrescriptionService;
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

@WebMvcTest(PrescriptionController.class)
class PrescriptionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrescriptionService prescriptionService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllPrescriptions() throws Exception {
        // Mock data
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        List<Prescription> prescriptions = new ArrayList<>();
        prescriptions.add(prescription);
        when(prescriptionService.getAllPrescriptions()).thenReturn(prescriptions);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetPrescriptionById() throws Exception {
        // Mock data
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(prescription);

        // Perform GET request with existing ID and verify response
        mockMvc.perform(get("/api/prescriptions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        // Perform GET request with non-existing ID and verify response
        mockMvc.perform(get("/api/prescriptions/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreatePrescription() throws Exception {
        // Mock data
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        when(prescriptionService.createPrescription(any(Prescription.class))).thenReturn(prescription);

        // Perform POST request with prescription data and verify response
        mockMvc.perform(post("/api/prescriptions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdatePrescription() throws Exception {
        // Mock data
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        when(prescriptionService.updatePrescription(eq(1L), any())).thenReturn(prescription);

        // Perform PUT request with prescription data and verify response
        mockMvc.perform(put("/api/prescriptions/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeletePrescription() throws Exception {
        when(prescriptionService.deletePrescription(1L)).thenReturn(true);
        // Perform DELETE request with prescription ID and verify response
        mockMvc.perform(delete("/api/prescriptions/1")
                    .with(csrf()))
                .andExpect(status().isNoContent());

        // Verify that service method is called with correct ID
        verify(prescriptionService, times(1)).deletePrescription(1L);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetPrescriptionByIdWhenNotFound() throws Exception {
        // Mock data - Prescription not found
        when(prescriptionService.getPrescriptionById(100L)).thenReturn(null);

        // Perform GET request with non-existing ID and verify response
        mockMvc.perform(get("/api/prescriptions/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdatePrescriptionWhenNotFound() throws Exception {
        // Mock data - Prescription not found
        when(prescriptionService.updatePrescription(100L, new Prescription())).thenReturn(null);

        // Perform PUT request with non-existing ID and verify response
        mockMvc.perform(put("/api/prescriptions/100")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeletePrescriptionWhenNotFound() throws Exception {
        when(prescriptionService.deletePrescription(100L)).thenReturn(false);
        // Perform DELETE request with non-existing ID and verify response
        mockMvc.perform(delete("/api/prescriptions/100")
                    .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
