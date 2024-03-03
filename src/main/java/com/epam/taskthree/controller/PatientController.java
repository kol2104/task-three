package com.epam.taskthree.controller;

import com.epam.taskthree.model.Patient;
import com.epam.taskthree.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    // Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    // Update an existing patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(id, patient);
        if (updatedPatient != null) {
            return ResponseEntity.ok(updatedPatient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        boolean deleted = patientService.deletePatient(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}