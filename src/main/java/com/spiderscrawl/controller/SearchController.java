package com.spiderscrawl.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spiderscrawl.model.Contacts;
import com.spiderscrawl.model.User;
import com.spiderscrawl.repository.ContactRepository;
import com.spiderscrawl.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal, Model model) {
		
		System.out.println(query);
		
		User userFromDb = userRepository.findUserByEmail(principal.getName());
		
		List<Contacts> contacts = contactRepository.findByNameContainingAndUser(query, userFromDb);
		
		model.addAttribute("contact", contacts);
		
		return ResponseEntity.ok(contacts);
		
		
	}

}
