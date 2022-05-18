package com.spiderscrawl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Authority implements Serializable{
	
	
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private int authorityId;
	
	private String authority;
	
	
	@ManyToMany(mappedBy = "authority")
	private List<User> user;

	

	public int getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(int authorityId) {
		this.authorityId = authorityId;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Authority [id=" + authorityId + ", authority=" + authority + "]";
	}	

}
