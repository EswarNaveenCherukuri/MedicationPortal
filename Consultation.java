package com.manage.hospital.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

// Model for consultation table
@Entity
@Table(name = "consultations")
public class Consultation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String date;
	private String problem;
	private String status;
	private int doctorId;
	
	// Relation between user and consultation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "medicationId", nullable = true)
	private Medication medication;

	private User doctor;

	// Constructors
	public Consultation() {
	}

	public Consultation(int id, String date, String problem, int doctorId, String status) {
		this.id = id;
		this.date = date;
		this.problem = problem;
		this.doctorId = doctorId;
		this.status = status;
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Medication getMedication() {
		return medication;
	}

	public void setMedication(Medication medication) {
		this.medication = medication;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setDoctor(User doctor) {
		this.doctor = doctor;
	}
	
	@Transient
	public User getDoctor() {
		return doctor;
	}

	
}