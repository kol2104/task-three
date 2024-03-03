package com.epam.taskthree.repository;

import com.epam.taskthree.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
