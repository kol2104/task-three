package com.epam.taskthree.service;

import com.epam.taskthree.model.Prescription;
import com.epam.taskthree.repository.PrescriptionRepository;
import com.epam.taskthree.service.impl.PrescriptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceImplTests {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;


    @Test
    void testGetAllPrescriptions() {
        // Mock data
        List<Prescription> prescriptions = new ArrayList<>();
        prescriptions.add(new Prescription());
        when(prescriptionRepository.findAll()).thenReturn(prescriptions);

        // Test
        List<Prescription> result = prescriptionService.getAllPrescriptions();

        // Verify
        assertEquals(1, result.size());
    }

    @Test
    void testGetPrescriptionById() {
        // Mock data
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));

        // Test
        Prescription result = prescriptionService.getPrescriptionById(1L);

        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetPrescriptionByIdWhenNotFound() {
        // Mock data - Prescription not found
        when(prescriptionRepository.findById(100L)).thenReturn(Optional.empty());

        // Test
        Prescription result = prescriptionService.getPrescriptionById(100L);

        // Verify
        assertNull(result);
    }

    @Test
    void testCreatePrescription() {
        // Mock data
        Prescription prescription = new Prescription();
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Test
        Prescription result = prescriptionService.createPrescription(prescription);

        // Verify
        assertNotNull(result);
    }

    @Test
    void testUpdatePrescriptionWhenExists() {
        // Mock data
        Long id = 1L;
        Prescription prescription = new Prescription();
        when(prescriptionRepository.existsById(id)).thenReturn(true);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Test
        Prescription result = prescriptionService.updatePrescription(id, prescription);

        // Verify
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testUpdatePrescriptionWhenNotExists() {
        // Mock data
        Long id = 1L;
        Prescription prescription = new Prescription();
        when(prescriptionRepository.existsById(id)).thenReturn(false);

        // Test
        Prescription result = prescriptionService.updatePrescription(id, prescription);

        // Verify
        assertNull(result);
    }

    @Test
    void testDeletePrescriptionWhenExists() {
        // Mock data
        Long id = 1L;
        when(prescriptionRepository.existsById(id)).thenReturn(true);

        // Test
        boolean result = prescriptionService.deletePrescription(id);

        // Verify
        assertTrue(result);
        verify(prescriptionRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeletePrescriptionWhenNotExists() {
        // Mock data
        Long id = 1L;
        when(prescriptionRepository.existsById(id)).thenReturn(false);

        // Test
        boolean result = prescriptionService.deletePrescription(id);

        // Verify
        assertFalse(result);
        verify(prescriptionRepository, never()).deleteById(id);
    }
}