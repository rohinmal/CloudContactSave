package com.spiderscrawl.services;

import java.util.Properties;

import javax.activation.MimeType;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transaction;

import org.springframework.stereotype.Service;


@Service
public class EmailService {
	
	public boolean sendEmail(String subject, String message, String to) {
		
		boolean f = false;
		
		String from = "rohinmaleywar1@gmail.com";
		
		String host = "smtp.gmail.com";
		
		Properties properties = System.getProperties();
		
		System.out.println("Properties  : "+properties);
		
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication("rohinmaleywar1@gmail.com", "delhi@12345");
			}
		});
		
		session.setDebug(true);		
				
		MimeMessage m = new MimeMessage(session);
		try {
			m.setFrom(from);
			
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			m.setSubject(subject);
			
			m.setContent(message, "text/html");
			
			Transport.send(m);
			
			System.out.println("sent successfully.....");
			
			f=true;
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return f;
		
	}

}
