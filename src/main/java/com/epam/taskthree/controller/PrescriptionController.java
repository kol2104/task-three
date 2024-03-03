package com.epam.taskthree.controller;

import com.epam.taskthree.model.Prescription;
import com.epam.taskthree.service.PrescriptionService;
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
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // Get all prescriptions
    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    // Get prescription by ID
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable("id") Long id) {
        Prescription prescription = prescriptionService.getPrescriptionById(id);
        if (prescription != null) {
            return ResponseEntity.ok(prescription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new prescription
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        Prescription savedPrescription = prescriptionService.createPrescription(prescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPrescription);
    }

    // Update an existing prescription
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable("id") Long id, @RequestBody Prescription prescription) {
        Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescription);
        if (updatedPrescription != null) {
            return ResponseEntity.ok(updatedPrescription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a prescription
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable("id") Long id) {
        boolean deleted = prescriptionService.deletePrescription(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}