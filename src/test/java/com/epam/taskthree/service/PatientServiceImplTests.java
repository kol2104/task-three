package com.epam.taskthree.service;

import com.epam.taskthree.model.Patient;
import com.epam.taskthree.repository.PatientRepository;
import com.epam.taskthree.service.impl.PatientServiceImpl;
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
class PatientServiceImplTests {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void testGetAllPatients() {
        // Mock data
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        when(patientRepository.findAll()).thenReturn(patients);

        // Test
        List<Patient> result = patientService.getAllPatients();

        // Verify
        assertEquals(1, result.size());
    }

    @Test
    void testGetPatientById() {
        // Mock data
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // Test
        Patient result = patientService.getPatientById(1L);

        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreatePatient() {
        // Mock data
        Patient patient = new Patient();
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Test
        Patient result = patientService.createPatient(patient);

        // Verify
        assertNotNull(result);
    }

    @Test
    void testUpdatePatientWhenExists() {
        // Mock data
        Long id = 1L;
        Patient patient = new Patient();
        when(patientRepository.existsById(id)).thenReturn(true);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Test
        Patient result = patientService.updatePatient(id, patient);

        // Verify
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testUpdatePatientWhenNotExists() {
        // Mock data
        Long id = 1L;
        Patient patient = new Patient();
        when(patientRepository.existsById(id)).thenReturn(false);

        // Test
        Patient result = patientService.updatePatient(id, patient);

        // Verify
        assertNull(result);
    }

    @Test
    void testDeletePatientWhenExists() {
        // Mock data
        Long id = 1L;
        when(patientRepository.existsById(id)).thenReturn(true);

        // Test
        boolean result = patientService.deletePatient(id);

        // Verify
        assertTrue(result);
        verify(patientRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeletePatientWhenNotExists() {
        // Mock data
        Long id = 1L;
        when(patientRepository.existsById(id)).thenReturn(false);

        // Test
        boolean result = patientService.deletePatient(id);

        // Verify
        assertFalse(result);
        verify(patientRepository, never()).deleteById(id);
    }

    @Test
    void testGetPatientByIdWhenNotFound() {
        // Mock data - Patient not found
        when(patientRepository.findById(100L)).thenReturn(Optional.empty());

        // Test
        Patient result = patientService.getPatientById(100L);

        // Verify
        assertNull(result);
    }
}