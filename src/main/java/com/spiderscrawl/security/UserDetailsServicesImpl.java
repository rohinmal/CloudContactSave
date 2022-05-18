package com.spiderscrawl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spiderscrawl.model.User;
import com.spiderscrawl.repository.UserRepository;

@Service
public class UserDetailsServicesImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findUserByEmail(username);
		
		if(user==null) {
			new UsernameNotFoundException("User Couldn't be found");
		}
		
		return new UserDetailsImpl(user);
	}

}
