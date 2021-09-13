package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.AdministratorAppApiApplication;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;


    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String email, String subject, String text) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("milosslaven96@gmail.com");
        mail.setSubject(subject);
        mail.setText(text);
        javaMailSender.send(mail);
    }

    public static void main(String[] args) {
        LocalDateTime date = LocalDateTime.parse("2022-09-09 23:20:39", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        LocalDate localDate = date.toLocalDate();

        java.util.Date date1 = Date.valueOf(localDate);

        System.out.println(date1);

    }

}
