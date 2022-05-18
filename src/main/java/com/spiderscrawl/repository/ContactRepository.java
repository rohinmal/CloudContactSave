package com.spiderscrawl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spiderscrawl.model.Contacts;
import com.spiderscrawl.model.User;

public interface ContactRepository extends JpaRepository<Contacts, Integer>{
	
	@Query("from Contacts as c where c.user.userId =:userId")
	Page<Contacts> findContactsByUser(int userId, Pageable page);
	
	
	List<Contacts> findByNameContainingAndUser(String name, User user);

}
