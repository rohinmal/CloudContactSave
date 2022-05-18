package com.spiderscrawl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private int userId;
	
	@NotEmpty(message= "name cannot be blank")
	@Size(min = 2, max = 20, message = "minimum 2 and maximum 10 characters are allowed")
	private String name;
	
	@NotEmpty(message = "Please provide gmail id")
	@Pattern(regexp = "[a-zA-Z0-9]+@gmail.com", message = "only valid gmail id's are allowed")
	private String email;
		
	@NotEmpty(message = "password cannot be blank")
//	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,20}$", 
//			 message = "password must starts with character should include digit, small letters, capital letters, "
//			 			+ " with minimum 6 digit and maximum 10 digit and space is not allowed")
	private String password;
	
	private boolean enabled;
	
	@NotEmpty(message="write something here")
	private String about;
	
	private String imageUrl;
	
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority",  joinColumns = @JoinColumn(name="userId"), inverseJoinColumns = @JoinColumn(name="authorityId")	)
	private List<Authority> authority;

	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<Contacts> contacts = new ArrayList<>();
	



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getAbout() {
		return about;
	}



	public void setAbout(String about) {
		this.about = about;
	}



	public List<Authority> getAuthority() {
		return authority;
	}



	public void setAuthority(List<Authority> authority) {
		this.authority = authority;
	}
	
	



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public List<Contacts> getContacts() {
		return contacts;
	}



	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}

	


	public String getImageUrl() {
		return imageUrl;
	}



	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", enabled=" + enabled + ", about=" + about + ", imageUrl=" + imageUrl + ", authority=" + authority
				+ "]";
	}





}
