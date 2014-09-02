package util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer
{
	public static void sendMail(String from,String to,String subject,String msg)throws Exception {     
		System.out.println("Mailer:---->sendMail()");
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "internal-mail-router.oracle.com");
		Session session = Session.getDefaultInstance(properties);
	      
		MimeMessage message = new MimeMessage(session);        
		message.setFrom(new InternetAddress(from));        
		message.addRecipients(Message.RecipientType.TO,to);
		message.setSubject(subject);
		message.setText(msg);
		Transport.send(message);
		System.out.println("Sent mail successfully....");      
   }
}