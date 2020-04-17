package com.manage.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manage.hospital.model.Consultation;

//Repository for Consultations
public interface ConsultationRepository extends JpaRepository<Consultation, Integer>{

	// Method to find all consultations with doctor id and status
	public List<Consultation> findAllByDoctorIdAndStatus(int doctorId, String status);
	
	// Method to find all consultations with user id and status
	public List<Consultation> findAllByUserIdAndStatus(int userId, String status);
	
	// Method to find all consultations with user id and not matching the status
	public List<Consultation> findAllByUserIdAndStatusNot(int userId, String status);
}
