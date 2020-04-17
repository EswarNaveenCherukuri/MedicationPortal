package com.manage.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manage.hospital.model.Medication;

//Repository for medication
public interface MedicationRepository extends JpaRepository<Medication, Integer>{

}
