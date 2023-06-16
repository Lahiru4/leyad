package util;

import dao.custom.AdminDAO;
import dao.custom.impl.AdminDAOImpl;
import model.AdminDTO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class SendMailWithAttachment {
    public SendMailWithAttachment(File file, String msg){
        sendMail(file,msg);
    }
    public SendMailWithAttachment(File file, String msg,String gmail_mail){
        sendMailDoctor(file,msg,gmail_mail);
    }
    public void sendMail(File file,String msg){
        AdminDAO adminDAO=new AdminDAOImpl();
        AdminDTO adminDTO =null;
        try {
            adminDTO = adminDAO.getAdmin();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String to = adminDTO.getGmail(); // to address. It can be any like gmail, hotmail etc.
        final String from = "teerakasuwa@gmail.com"; // from address. As this is using Gmail SMTP.
        final String password = "zmojxpsytszfivim"; // password for from mail address.

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Theeraka Suwa Sewa");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            String hed_mg="<html lang=\"en\">\n" +
                    "<head></head><body><h2>"+msg+"</h2></body></html>";

            mimeBodyPart.setContent(hed_mg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(file);
            multipart.addBodyPart(attachmentBodyPart);
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Mail successfully sent..");


        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendMailDoctor(File file,String msg,String gmail){
        AdminDAO adminDAO=new AdminDAOImpl();
        AdminDTO adminDTO =null;
        try {
            adminDTO = adminDAO.getAdmin();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String to = gmail; // to address. It can be any like gmail, hotmail etc.
        final String from = "teerakasuwa@gmail.com"; // from address. As this is using Gmail SMTP.
        final String password = "zmojxpsytszfivim"; // password for from mail address.

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Theeraka Suwa Sewa");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            String hed_mg="<html lang=\"en\">\n" +
                    "<head></head><body><h2>"+msg+"</h2></body></html>";

            mimeBodyPart.setContent(hed_mg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(file);
            multipart.addBodyPart(attachmentBodyPart);
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Mail successfully sent..");


        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
