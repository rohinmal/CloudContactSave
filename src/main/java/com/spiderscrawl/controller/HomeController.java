package com.spiderscrawl.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spiderscrawl.model.Authority;
import com.spiderscrawl.model.User;
import com.spiderscrawl.repository.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping
	public String home() {
		
		return "home";
	}
	
	@GetMapping("/about")
	public String about() {
		
		return "about";
	}
	
	@GetMapping("/signin")
	public String signin(Model model) {
		
		model.addAttribute("user", new User());
		return "login";
	}
	@GetMapping("/signup")
	public String signup(Model model) {
	
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String doRegister(@Validated @ModelAttribute("user") User user, BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			System.out.println("Errors : "+result.toString());
			model.addAttribute("user", user);
			return "signup";
		}
		
		if(user!=null) {
			
			Authority authority = new Authority();
			authority.setAuthority("USER");
			
			List<Authority> auth = new ArrayList<>();
			auth.add(authority);

			user.setPassword(passwordEncoder.encode(user.getPassword()));

			user.setAuthority(auth);
			user.setEnabled(true);
	
			userRepository.save(user);
			 return "redirect:/signin?success";
		}
		
		return "signup";
	}
	
	@GetMapping("/dologin")
	public String dologin(@ModelAttribute("user") User user, Model model, Principal principal) {
		
		
		
		model.addAttribute("user", user);
		
		System.out.println(principal.getName());
		
		return "normal/user_dashboard";
	}
		
}
