package com.spiderscrawl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.spiderscrawl.model.Authority;
import com.spiderscrawl.model.Contacts;
import com.spiderscrawl.model.User;
import com.spiderscrawl.repository.ContactRepository;
import com.spiderscrawl.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private ContactRepository contactRepository;

	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/add-contact")
	public String addContact(Model model, Principal principal) {
		
		String loggedInUser = principal.getName();
		
		User userFromDB = userRepository.findUserByEmail(loggedInUser);
		
		
		
		model.addAttribute("contact", new Contacts());
		model.addAttribute("user", userFromDB);
		
		
//		model.addAttribute("user", new User());
		
		return "normal/add_contact_form";
		
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") Contacts contact, @RequestParam("profileImage") MultipartFile file, Principal principal) {
		
		
		try {
		
		if(file.isEmpty()) {
			System.out.println("file is empty");
			contact.setImageName("contact.png");
		}
		else {
			
			String fileName = file.getOriginalFilename();
			
			File saveFile =	 new ClassPathResource("static/img").getFile();
			
			Path path =  Paths.get(saveFile.getAbsolutePath() + File.separator+fileName);
			
			System.out.println(path);
			
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("file is uploaded");
			
			contact.setImageName(fileName);
		
		}
		
		String loggedUserEmail = principal.getName();

		System.out.println(loggedUserEmail);
		
		User userFromDb = userRepository.findUserByEmail(loggedUserEmail);
		
		userFromDb.getContacts().add(contact);
		
		contact.setUser(userFromDb);
		
		userRepository.save(userFromDb);
		
		}catch(IOException e) {
			e.printStackTrace();
		}	
		
		return "redirect:/user/show-contacts/0";
	}
	
	@GetMapping("/show-contacts/{page}")
	public String showContact(@PathVariable("page")Integer page,  Principal principal, Model m) {
		m.addAttribute("title", "Show User Contacts");
		// contact ki list ko bhejni hai

		String userName = principal.getName();

		User user = this.userRepository.findUserByEmail(userName);
		
		System.out.println(page);
		
		// currentPage-page
		// Contact Per page - 5
		Pageable pageable = PageRequest.of(page, 3);

		Page<Contacts> contacts = this.contactRepository.findContactsByUser(user.getUserId(), pageable);

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		m.addAttribute("user", user);

		return "normal/show_contacts";
	}
	
	@GetMapping("/index")
	public String userDashBoard(Model model, Principal principal) {
		
		model.addAttribute("user", new User());
		
		System.out.println(principal);
		
		return "normal/user_dashboard";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id")int id, Principal principal) {
		
		System.out.println("delted id :"+id);
		String loggedInUser = principal.getName();
		
		Contacts contacts2 = contactRepository.findById(id).get();
		
		System.out.println("TO be deleted :"+contacts2);
		User userFromDb = userRepository.findUserByEmail(loggedInUser);
		
		userFromDb.getContacts().remove(contacts2);
		
		List<Contacts> contacts = userFromDb.getContacts();
		
		
		System.out.println("remain contacts :"+contacts);
		
		userRepository.save(userFromDb);
		
		System.out.println("DELETED");
		
		return "redirect:/user/show-contacts/0";
	}
	
	@PostMapping("/update-contact/{id}")
	public String updateContact(@PathVariable("id") int id, Model model, Principal principal) {
		
		Contacts contacts = contactRepository.findById(id).get();
		
		User userFromDb = userRepository.findUserByEmail(principal.getName());
		
		model.addAttribute("contact", contacts);
		model.addAttribute("user", userFromDb);

		
		return "normal/update_form";
	}
	
	@PostMapping("/process-update")
	public String processUpdate(@ModelAttribute("contact") Contacts contact, @RequestParam("profileImage") MultipartFile file, Principal principal) {
		
		System.out.println("Contact Filled :"+contact);
		
		System.out.println("File Image Details :"+file.getOriginalFilename());
		
	Contacts contactFromDb = contactRepository.findById(contact.getcId()).get();
	
	
	
	contactFromDb.setDescription(contact.getDescription());
	contactFromDb.setEmail(contact.getEmail());
	contactFromDb.setName(contact.getName());
	contactFromDb.setPhone(contact.getPhone());
	contactFromDb.setSecondName(contact.getSecondName());
	contactFromDb.setWork(contact.getWork());
	
	try {
		if(!file.isEmpty()) {
		
			//deleting old image
			File oldFile = new ClassPathResource("static/img").getFile();
			
			File oldFileObject = new File(oldFile, contactFromDb.getImageName());
			oldFileObject.delete();
			
			
			//uploading new image
			
			String fileName = file.getOriginalFilename();
			
			System.out.println("File Name :"+fileName);
			
			File file2 = new ClassPathResource("static/img").getFile();
			
			
			
			Path path = Paths.get(file2.getAbsolutePath()+File.separator+fileName);
			
			System.out.println("File Path :"+path);
			
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			contactFromDb.setImageName(fileName);
		}else {
			
				String imageName = contactFromDb.getImageName();
				contactFromDb.setImageName(imageName);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	System.out.println("DB contact :"+contactFromDb);
	
	User userFromDb = userRepository.findUserByEmail(principal.getName());
	
	userFromDb.getContacts().add(contactFromDb);
	
	contactFromDb.setUser(userFromDb);
	
	userRepository.save(userFromDb);
	
		
		
		return "redirect:/user/show-contacts/0";
			
	}
	
	@GetMapping("{id}/contact")
	public String showContactDetails(@PathVariable("id")int id, Principal principal, Model model) {
		
		User userFromDb = userRepository.findUserByEmail(principal.getName());
		
		Contacts contactFromDb = contactRepository.getById(id);
		
		model.addAttribute("contact", contactFromDb);
		model.addAttribute("user", userFromDb);
		
		return "normal/contact_detail";
	}
	
	@GetMapping("/profile")
	public String userProfile(Principal principal, Model model) {
		
		User userFromDb = userRepository.findUserByEmail(principal.getName());
		
		
		
		model.addAttribute("user", userFromDb);
		
		return "normal/profile";
	}
	
	@GetMapping("/settings")
	public String setting(Principal principal, Model model) {
		
		User userByDb = userRepository.findUserByEmail(principal.getName());
		model.addAttribute("user", userByDb);
		
		
		return "normal/settings";
	}
	
	@PostMapping("/change-password")
	public String passwordChange(@RequestParam("oldPassword") String oldPassword, 
						@RequestParam("newPassword") String newPassword, Principal principal) {
		
		System.out.println("oldpassword : "+oldPassword+", newPassword : "+newPassword);
		
		User userFromDb = userRepository.findUserByEmail(principal.getName());
		if(!oldPassword.equals("") && !newPassword.equals("")) {
			if(passwordEncoder.matches(oldPassword, userFromDb.getPassword())) {
				
				userFromDb.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(userFromDb);
				return "redirect:/signin?change";
			}else {
				return "redirect:/user/settings?message";
			}
		}else {
			if(oldPassword.equals("")) {
				return "redirect:/user/settings?message1";
			}
			if(newPassword.equals("")) {
				return "redirect:/user/settings?message2";
			}else {
				return "redirect:/user/settings?message3";
			}

		}
		
		
	}
	

	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data) throws Exception
	{
		//System.out.println("Hey order function ex.");
		System.out.println(data);
		
		int amt=Integer.parseInt(data.get("amount").toString());
		
		var client=new RazorpayClient("rzp_test_rJEP6YpC8hcjCb", "H4ZOEXlyePWs1Od2aDNcjb4C");
		
		JSONObject ob=new JSONObject();
		ob.put("amount", amt*100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_235425");
		
		//creating new order
		
		Order order = client.Orders.create(ob);
		System.out.println(order);
		
		//if you want you can save this to your data..		
		return order.toString();
	}
		
}
	