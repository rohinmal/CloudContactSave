package com.spiderscrawl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@ComponentScan("com.spiderscrawl")
@EnableWebMvc
public class CloudLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudLoggingApplication.class, args);
	}

}
