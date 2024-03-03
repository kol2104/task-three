package com.epam.taskthree.service.impl;

import com.epam.taskthree.model.Prescription;
import com.epam.taskthree.repository.PrescriptionRepository;
import com.epam.taskthree.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).orElse(null);
    }

    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Prescription updatePrescription(Long id, Prescription prescription) {
        if (prescriptionRepository.existsById(id)) {
            prescription.setId(id);
            return prescriptionRepository.save(prescription);
        } else {
            return null;
        }
    }

    public boolean deletePrescription(Long id) {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}