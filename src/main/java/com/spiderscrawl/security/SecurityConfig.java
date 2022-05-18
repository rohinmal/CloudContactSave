package com.spiderscrawl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity(debug=true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsServicesImpl userDetailsServicesImpl;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		
		DaoAuthenticationProvider daoAuthentication = new DaoAuthenticationProvider();
		daoAuthentication.setPasswordEncoder(passwordEncoder());
		daoAuthentication.setUserDetailsService(userDetailsServicesImpl);
		return daoAuthentication;
	
	}	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
			.authenticationProvider(daoAuthenticationProvider());
		
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http	
			.authorizeRequests().antMatchers("/about", "/signup", "/do_register", "/img", "/css", "/js").permitAll()
			.and()
			.formLogin().loginPage("/signin").loginProcessingUrl("/process-success").defaultSuccessUrl("/dologin").permitAll()
			.and()
			.httpBasic()
			.and()
			.logout().permitAll()
			.and()
			.csrf().disable();
	}
	
}
