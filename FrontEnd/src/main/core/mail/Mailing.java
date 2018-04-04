package main.core.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailing {
	static String port;
	static String host;
	static String user;
	static String pass;
	static String to;
	static String from;

	public static boolean sendMail(INotificationTemplate message, String level) {
		Settings.updateSettings();
		System.out.println("setting up mail");
		Properties props = new Properties();
		props.setProperty("mail.debug", "true"); // Enable debug debugging
		props.setProperty("mail.smtp.auth", "true"); // The sending server requires authentication
		props.setProperty("mail.host", Settings.getHost()); // Set the mail server host name smtp.gmail.com
		props.setProperty("mail.transport.protocol", "smtp"); // Send mail protocol name
		props.setProperty("mail.smtp.auth.mechanisms", "login");
		props.put("mail.smtp.port", Settings.getPort()); // 587
		props.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(props, new Authenticator() {
			// Set the account information session，transport will send mail
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Settings.getUser(), Settings.getPass());
			}
		});
		session.setDebug(true);

		Message msg = new MimeMessage(session);
		try {
			msg.setSubject(message.getSubject()); // Set the mail subject
			msg.setContent(message.getTemplate(), "text/html;charset=utf-8");
			msg.setFrom(new InternetAddress(Settings.getFrom())); // Set the sender
			if (level.equalsIgnoreCase("1"))
				msg.setRecipient(RecipientType.TO, new InternetAddress(Settings.getLVL1_NOTIFICATION())); // Set the
																											// recipient
			else
				msg.setRecipient(RecipientType.TO, new InternetAddress(Settings.getLVL2_NOTIFICATION())); // Set the
																											// recipient
			Transport.send(msg);
			System.out.println("mail sent successfully");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			return false;
		}

	}

	public static boolean validateSaveMailSettings(String host, String port, String user, String pass, String from,
			String to, String lvl) {
		Mailing.port = port;
		Mailing.host = host;
		Mailing.user = user;
		Mailing.pass = pass;
		Mailing.to = to;
		Mailing.from = from;

		return true;
	}

}
