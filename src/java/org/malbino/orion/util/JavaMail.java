/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.util;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author malbino
 */
public class JavaMail implements Runnable {

    private final Session mailSession;
    private final List<String> to;
    private final String subject;
    private final String html;

    public JavaMail(List<String> to, String subject, String html) throws NamingException {
        InitialContext ctx = new InitialContext();
        mailSession = (Session) ctx.lookup("orionMailing");

        this.to = to;
        this.subject = subject;
        this.html = html;
    }

    public void sendEmail() {
        try {
            MimeMessage message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(mailSession.getProperty("mail.from")));
            InternetAddress[] address = new InternetAddress[to.size()];
            for (int i = 0; i < to.size(); i++) {
                address[i] = new InternetAddress(to.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(html, "text/html");
            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(JavaMail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(JavaMail.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        this.sendEmail();
    }
}
