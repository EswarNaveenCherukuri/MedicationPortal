package com.manage.hospital.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.manage.hospital.model.Consultation;
import com.manage.hospital.model.Order;
import com.manage.hospital.model.User;
import com.manage.hospital.service.ConsultationService;
import com.manage.hospital.service.UserService;

@Controller
@SessionAttributes("signedInUser")
public class ConsultationController {

	@Autowired
	private ConsultationService consultationService;
	@Autowired
	private UserService userService;

	// Consult page mapping
	@GetMapping("/consult")
	public String getConsultView(Map<String, Object> model, RedirectAttributes redirectAttributes) {
		// Will show consult page only if user logged in
		if (model.containsKey("signedInUser")) {
			model.put("consultation", new Consultation());
			model.put("doctorList", userService.findAllByRole());
			return "consult";
		} else {
			// If user not logged in redirect to sign in page
			redirectAttributes.addFlashAttribute("fromConsult", true);
			return "redirect:/signin";
		}
	}

	// Mapping for new consultation
	@PostMapping("/addConsultation")
	public String addConsultation(Map<String, Object> model, @ModelAttribute Consultation consultation,
			RedirectAttributes redirectAttributes) {
		// Consulting can be done only by logged in user
		if (model.containsKey("signedInUser")) {
			User user = (User) model.get("signedInUser");
			consultation.setUser(user);
			// Adding the consultation
			String status = consultationService.addConsultation(consultation);
			if (status.length() == 0) {
				// Redirect to consults if success
				redirectAttributes.addFlashAttribute("fromAddConsultation", true);
				return "redirect:/viewconsultations";
			} else {
				model.put("consultationError", status);
				return "consult";
			}
		} else {
			// If not logged in redirect to sign in page
			redirectAttributes.addFlashAttribute("fromAddConsultation", true);
			return "redirect:/signin";
		}
	}

	// Prescription page mapping
	@GetMapping("/prescribe")
	public String getPrescribeView(Map<String, Object> model, RedirectAttributes redirectAttributes) {
		// Will show prescribe page only if user logged in
		if (model.containsKey("signedInUser")) {
			model.put("selectedConsultation", new Consultation());
			User currentUser = (User) model.get("signedInUser");
			List<Consultation> consultationList = consultationService
					.getAllConsultationsByDoctorId(currentUser.getId());
			model.put("consultationList", consultationList);
			model.put("medicationList", consultationService.getAllMedications());
			return "prescribe";
		} else {
			// If user not logged in redirect to sign in page
			redirectAttributes.addFlashAttribute("fromPrescribe", true);
			return "redirect:/signin";
		}
	}

	// Mapping for new medication
	@PostMapping("/addMedication")
	public String addMedication(Map<String, Object> model, @ModelAttribute Consultation selectedConsultation,
			RedirectAttributes redirectAttributes) {
		// Adding the Medication
		String status = consultationService.addMedication(selectedConsultation);
		if (status.length() == 0) {
			// Redirect to prescribe if success
			redirectAttributes.addFlashAttribute("fromAddMedication", true);
			return "redirect:/prescribe";
		} else {
			model.put("medicationError", status);
			return "prescribe";
		}

	}

	// View Consultations page mapping
	@GetMapping("/viewconsultations")
	public String getViewConsultations(Map<String, Object> model, RedirectAttributes redirectAttributes) {
		// Will show view consultations page only if user logged in
		if (model.containsKey("signedInUser")) {
			User currentUser = (User) model.get("signedInUser");
			// Populating list of consultations which have not been prescribed
			List<Consultation> pendingConsultationList = consultationService
					.getAllPendingUserConsultations(currentUser.getId());
			model.put("pendingConsultationList", pendingConsultationList);
			// Populating list of consultations which have been prescribed
			List<Consultation> completedConsultationList = consultationService
					.getAllCompletedUserConsultations(currentUser.getId());
			model.put("completedConsultationList", completedConsultationList);
			return "viewconsultations";
		} else {
			// If user not logged in redirect to sign in page
			redirectAttributes.addFlashAttribute("fromViewConsultations", true);
			return "redirect:/signin";
		}
	}

	// Order Medication page mapping
	@GetMapping("/ordermedication")
	public String getOrderMedicationView(Map<String, Object> model, RedirectAttributes redirectAttributes) {
		// Will show consult page only if user logged in
		if (model.containsKey("signedInUser")) {
			model.put("selectedConsultation", new Consultation());
			User currentUser = (User) model.get("signedInUser");
			List<Consultation> consultationList = consultationService
					.getAllPrescribedUserConsultations(currentUser.getId());
			model.put("consultationList", consultationList);
			return "ordermedication";
		} else {
			// If user not logged in redirect to sign in page
			redirectAttributes.addFlashAttribute("fromOrderMedication", true);
			return "redirect:/signin";
		}
	}

	// Mapping for new order
	@PostMapping("/addOrder")
	public String addOrder(Map<String, Object> model, @ModelAttribute Consultation selectedConsultation,
			RedirectAttributes redirectAttributes) {
		String status = consultationService.addOrder(selectedConsultation);
		if (status.length() == 0) {
			// Redirect to order if success
			redirectAttributes.addFlashAttribute("fromAddOrder", true);
			return "redirect:/vieworders";
		} else {
			model.put("orderError", status);
			return "ordermedication";
		}

	}

	// View Orders page mapping
	@GetMapping("/vieworders")
	public String getViewOrders(Map<String, Object> model, RedirectAttributes redirectAttributes) {
		// Will show view consultations page only if user logged in
		if (model.containsKey("signedInUser")) {
			User currentUser = (User) model.get("signedInUser");
			// Populating list of orders
			List<Order> orderList = consultationService.getAllOrders(currentUser.getId());
			model.put("orderList", orderList);
			return "vieworders";
		} else {
			// If user not logged in redirect to sign in page
			redirectAttributes.addFlashAttribute("fromViewOrders", true);
			return "redirect:/signin";
		}
	}
}
