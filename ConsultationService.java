package com.manage.hospital.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.hospital.model.Consultation;
import com.manage.hospital.model.Medication;
import com.manage.hospital.model.Order;
import com.manage.hospital.model.User;
import com.manage.hospital.repository.ConsultationRepository;
import com.manage.hospital.repository.MedicationRepository;
import com.manage.hospital.repository.OrderRepository;

@Service
public class ConsultationService {

	@Autowired
	private ConsultationRepository consultationRepository;
	@Autowired
	private MedicationRepository medicationRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserService userService;

	// Method to create new consultation
	public String addConsultation(Consultation consultation) {
		String message = "";
		try {
			// Adding new consult
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			consultation.setDate(formatter.format(date));
			consultation.setStatus("Pending");
			consultation = consultationRepository.save(consultation);
		} catch (Exception e) {
			// On fail
			message = "Please try again";
		}
		return message;
	}

	// Method to get all the consultations of doctor
	public List<Consultation> getAllConsultationsByDoctorId(int doctorId) {
		return consultationRepository.findAllByDoctorIdAndStatus(doctorId, "Pending");
	}

	// Method to get all the medications
	public List<Medication> getAllMedications() {
		return medicationRepository.findAll();
	}

	// Method to add medication for a consultation
	public String addMedication(Consultation selectedConsultation) {

		String message = "";
		try {
			Consultation consultation = consultationRepository.findById(selectedConsultation.getId()).orElse(null);
			// Updating the consultation with the medication and status
			consultation.setStatus("Prescribed");
			consultation.setMedication(selectedConsultation.getMedication());
			consultationRepository.save(consultation);
		} catch (Exception e) {
			// On fail
			message = "Please try again";
		}
		return message;
	}

	// Method to get all the pending consultations of the user
	public List<Consultation> getAllPendingUserConsultations(int userId) {
		List<Consultation> pendingConsultationList = consultationRepository.findAllByUserIdAndStatus(userId, "Pending");
		for (Consultation consultation : pendingConsultationList) {
			User doctor = userService.getUserById(consultation.getDoctorId());
			consultation.setDoctor(doctor);
		}
		return pendingConsultationList;
	}

	// Method to get all the prescribed consultations of the user
	public List<Consultation> getAllPrescribedUserConsultations(int userId) {
		return consultationRepository.findAllByUserIdAndStatus(userId, "Prescribed");
	}

	// Method to get all the prescribed and ordered consultations of the user
	public List<Consultation> getAllCompletedUserConsultations(int userId) {
		List<Consultation> completedConsultationList = consultationRepository.findAllByUserIdAndStatusNot(userId,
				"Pending");
		if (completedConsultationList.size() != 0) {
			for (Consultation consultation : completedConsultationList) {
				User doctor = userService.getUserById(consultation.getDoctorId());
				consultation.setDoctor(doctor);
			}
		}
		return completedConsultationList;
	}

	// Method to create new order
	public String addOrder(Consultation selectedConsultation) {
		String message = "";
		try {
			Consultation consultation = consultationRepository.findById(selectedConsultation.getId()).orElse(null);
			// Adding new order
			Order order = new Order();
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			order.setDate(formatter.format(date));
			formatter = new SimpleDateFormat("HH:mm");
			order.setTime(formatter.format(date));
			order.setMedication(consultation.getMedication());
			order.setUser(consultation.getUser());
			consultation.setStatus("Ordered");
			consultation = consultationRepository.save(consultation);
			order = orderRepository.save(order);
		} catch (Exception e) {
			// On fail
			message = "Please try again";
		}
		return message;
	}
	
	// Method to get all the ordered consultations
	public List<Order> getAllOrders(int userId) {
		return orderRepository.findAllByUserId(userId);
	}

}
