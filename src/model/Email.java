/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.HeadlessException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author luiz.pereira
 */
public class Email {
    private String emailDestinatario;
    private String corpoEmail;

    public Email(){}
    
    public Email(String emailDestinatario){
        this.emailDestinatario = emailDestinatario;
    }

    public String getEmailDestinatario() {
        return emailDestinatario;
    }

    public void setEmailDestinatario(String emailDestinatario) {
        this.emailDestinatario = emailDestinatario;
    }

    public String getCorpoEmail() {
        return corpoEmail;
    }

    public void setCorpoEmail(String corpoEmail) {
        this.corpoEmail = corpoEmail;
    }
    
    public void enviarEmail() throws AddressException, MessagingException {
        try {
            //config. do gmail
            Properties mailProps = new Properties();
            mailProps.put("mail.transport.protocol", "smtp");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.host", "smtp.gmail.com");
            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.user", "email_remetente@remetente.com.br");
            mailProps.put("mail.debug", "true");
            mailProps.put("mail.smtp.port", "465");
            mailProps.put("mail.smtp.socketFactory.port", "465");
            mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProps.put("mail.smtp.socketFactory.fallback", "false");

            //autenticação
            Session mailSession = Session.getInstance(mailProps, new javax.mail.Authenticator() {
                @Override
                public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication("email_remetente@remetente.com.br", "senha_remetente");
                }
            });
            
            //setando configs. do e-mail
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("email_remetente@remetente.com.br"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.emailDestinatario));

            //setando assunto
            message.setSubject("DBMonitoring - Transações Pendentes");
            
            //setando tipo do texto do corpo de e-mail
            message.setContent(this.corpoEmail, "text/html; charset=utf-8");

            //envia o e-mail
            Transport.send(message);
        } catch (HeadlessException e) {
            System.out.println(e.getMessage().trim());
        }
        System.gc();
    }
}
