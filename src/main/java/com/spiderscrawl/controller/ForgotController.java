package com.spiderscrawl.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spiderscrawl.model.User;
import com.spiderscrawl.repository.UserRepository;
import com.spiderscrawl.services.EmailService;

@Controller
public class ForgotController {
	
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/forgot")
	public String forgot() {
		
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		
		System.err.println("Email : "+email);
		
		Random r = new Random(1000);
		
		int otp = r.nextInt(999999);
		
		String subject = "Sent from Cloud Messaging";
		
		String message = ""
				+ "<div style='border:1px solid #e2e2e2; padding:20px'>"
				+ "<h1>"
				+ "OTP is "
				+ "<b>"+otp
				+ "</n>"
				+ "</h1> "
				+ "</div>";
		
		String to = email;
		
		
		boolean flag = emailService.sendEmail(subject, message, to);
		
		if(flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}else {
			session.setAttribute("message", "check your email id");
			
			return "forgot_email_form";
		}
			
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		
		System.out.println("Otp: "+otp);
		
		int myotp = (int)session.getAttribute("myotp");
		String userEnteredEmail = (String)session.getAttribute("email");
		
		User userfromDb = userRepository.findUserByEmail(userEnteredEmail);
		
		if(otp==myotp) {
			
			if(userfromDb==null) {
				
				session.setAttribute("nouserfound", "Incorrect email id, Please enter correct email id");
				return "forgot_email_form";
			}else {
				session.setAttribute("correctemail", "Please change your password");
				return "password_change_form";
			}
		}else {
			
			session.setAttribute("wrongotp", "Wrong OTP, Please enter again...");
			return "verify_otp";
		}	
	}
	
	@PostMapping("/change-password")
	public String changedPassword(@RequestParam("newpassword") String password, HttpSession session) {
		
		String userEnteredEmail = (String)session.getAttribute("email");
		
		User userFromDb = userRepository.findUserByEmail(userEnteredEmail);

		userFromDb.setPassword(passwordEncoder.encode(password));
		
		userRepository.save(userFromDb);
		
		return "redirect:/signin?change";
	}
}
