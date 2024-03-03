package com.epam.taskthree.service;

import com.epam.taskthree.model.Prescription;

import java.util.List;

public interface PrescriptionService {
    List<Prescription> getAllPrescriptions();
    Prescription getPrescriptionById(Long id);
    Prescription createPrescription(Prescription prescription);
    Prescription updatePrescription(Long id, Prescription prescription);
    boolean deletePrescription(Long id);

}
