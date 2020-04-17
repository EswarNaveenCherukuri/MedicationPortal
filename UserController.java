package com.manage.hospital.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.manage.hospital.model.User;
import com.manage.hospital.service.UserService;

@Controller
@SessionAttributes("signedInUser")
public class UserController {

	@Autowired
	private UserService userService;

	// Sign up page mapping
	@GetMapping("/signup")
	public String getRegistrationView(Map<String, Object> model) {
		model.put("signup", new User());
		return "signup";
	}

	// Sign in page mapping
	@GetMapping("/signin")
	public String getLoginView(Map<String, Object> model, RedirectAttributes redirectAttributes) {
		// Setting response attributes to show warning / success messages
		if (redirectAttributes.containsAttribute("fromRegistration")) {
			model.put("fromRegistration", true);
		}
		model.put("signin", new User());
		return "signin";
	}

	@PostMapping("/register")
	public String register(Map<String, Object> model, @ModelAttribute User user,
			RedirectAttributes redirectAttributes) {
		String status = userService.addUser(user);
		if (status.length() == 0) {
			// Redirecting to sign in on success
			redirectAttributes.addFlashAttribute("fromRegistration", true);
			return "redirect:/signin";
		} else {
			// Back to sign up page on error
			model.put("signup", user);
			model.put("signUpError", status);
			return "signup";
		}
	}
	
	// Mapping for logging the user
		@PostMapping("/login")
		public String login(Map<String, Object> model, @ModelAttribute User user) {
			User found = userService.authenticate(user);
			if (found != null) {
				// Successfully logged in
				model.put("signedInUser", found);
				if(found.getRole().equals("doctor")) {
					return "redirect:/prescribe";
				} else {
					return "redirect:/consult";
				}
			} else {
				// Redirecting back to sign due to authentication error
				model.put("signin", user);
				model.put("signInError", "Incorrect Email / Password");
				return "signin";
			}
		}
		
		// Mapping for sign out
		@GetMapping("/signout")
		public String logout(Map<String, Object> model, SessionStatus status) {
			// Clearing session
			model.remove("signedInUser");
			status.setComplete();
			return "redirect:/";
		}
}
