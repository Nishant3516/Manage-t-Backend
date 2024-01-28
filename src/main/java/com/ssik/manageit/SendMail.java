package com.ssik.manageit;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	public static void main(String[] args) {
// jlgmirccksaoykaw --Google app password.

		// Recipient's email ID needs to be mentioned.
		String to = "cpandey05@gmail.com";
//		   host: smtp.mail.yahoo.com
//		    port: 587
//		    username: manageit.ssik@yahoo.com  #Replace this field with your Yahoo username.
//		    password: P@ssword005abc           #Replace this field with your Yahoo password.
//		    protocol: smtp
//		    tls: true
//		    properties.mail.smtp:
//		        auth: true
//		        starttls.enable: true
//		        ssl.trust: smtp.mail.yahoo.com

		// Sender's email ID needs to be mentioned
		String from = "sms.ssik@gmail.com";
		String appPassword = "jlgmirccksaoykaw";

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
//		properties.put("mail.smtp.host", host);
//		properties.put("mail.smtp.port", "587");
//		properties.put("mail.smtp.ssl.enable", "true");
//		properties.put("mail.smtp.auth", "true");
//		properties.put("mail.smtp.starttls.enable", "true");
//		properties.put("mail.smtp.ssl.trust", "smtp.mail.yahoo.com");

		// Setup mail server
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", from);
		// properties.put("mail.smtp.password", "vnautqmxxwgbpjnx");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		// properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(from, appPassword);

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");

			// Now set the actual message
			message.setText("This is actual message");

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

}