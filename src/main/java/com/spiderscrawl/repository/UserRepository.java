package com.spiderscrawl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spiderscrawl.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findUserByEmail(String email);
}
