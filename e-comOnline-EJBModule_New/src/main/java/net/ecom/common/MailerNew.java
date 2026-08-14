/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailerNew {

    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String sub;
    private String content;
    private MimeMessage message;
    private Session session;
    private Multipart multipart = new MimeMultipart();
    private MimeBodyPart bodyPart;
    private DataSource source;
    private int attachCnt = 0;
    private String fname;
    private String name;
    public Hashtable tab = new Hashtable();
    private String[] to1;
    private String tmpName = " ";
    private int i = 0;
    public boolean blnSend = true;
    Validation validate = new Validation();
    private String MAIL_ID = "";
    private String PWD = "";
    private String HOST = "";

    class TransportThread //extends Thread
    {

        private void mailSent() throws Exception {
            try {
                //System.out.println("[ECOM] MailerNew mail sent method calling ");
                Transport.send(message);
                blnSend = true;
            } catch (MessagingException exm) {
                System.out.println("[ECOM] MailerNew With in messaging exception " + exm);
                Exception excep = exm;
                blnSend = false;
                throw exm;
            } catch (Exception e) {
                blnSend = false;
                throw e;
            }
        }
    }

    public MailerNew() {
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSubject(String subject) {
        this.sub = subject;
    }

    public void setMessage(String message) {
        this.content = message;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public void sendMail(HashMap auth) throws Exception {
        //System.out.println("[ECOM] MailerNew *************************test12222222222");
        if (from.trim().length() != 0 && to.trim().length() != 0 && auth != null && auth.size() >= 3) {
            MAIL_ID = validate.checkNull((String) auth.get("MAIL_ID"));
            PWD = validate.checkNull((String) auth.get("PASSWORD"));
            HOST = validate.checkNull((String) auth.get("SMTP_HOST"));
            System.out.println("[ECOM] MailerNew ******MAIL********==>" + MAIL_ID);
            Properties prop = new Properties();

            prop.put("mail.smtp.host", HOST);

            javax.mail.Authenticator pa = null;

            prop.put("mail.smtp.auth", "true");

            pa = new javax.mail.Authenticator() {

                public javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MAIL_ID, PWD);
                }
            };
            session = Session.getInstance(prop, pa);

            message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress(from));
                String[] str = to.split(",");
                Address[] toAdd = new InternetAddress[str.length];
                int i = 0;
                for (i = 0; i < str.length; i++) {
                    toAdd[i] = new InternetAddress(str[i]);
                }
                for (i = 0; i < str.length; i++) {
                    message.addRecipient(Message.RecipientType.TO, toAdd[i]);
                }
                if (cc != null && cc.length() > 0) {
                    String[] strcc = cc.split(",");
                    Address[] ccAdd = new InternetAddress[strcc.length];
                    int j = 0;
                    for (j = 0; j < strcc.length; j++) {
                        ccAdd[j] = new InternetAddress(strcc[j]);
                    }
                    for (j = 0; j < strcc.length; j++) {
                        message.addRecipient(Message.RecipientType.CC, ccAdd[j]);
                    }
                }
                if (bcc != null && bcc.length() > 0) {
                    String[] strbcc = bcc.split(",");
                    Address[] bccAdd = new InternetAddress[strbcc.length];
                    int j = 0;
                    for (j = 0; j < strbcc.length; j++) {
                        bccAdd[j] = new InternetAddress(strbcc[j]);
                    }
                    for (j = 0; j < strbcc.length; j++) {
                        message.addRecipient(Message.RecipientType.BCC, bccAdd[j]);
                    }
                }
                message.setSubject(sub);
                message.setSentDate(new Date());
                // message.setText(this.content);
                //  message.setContent(this.content, "text/html");
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(this.content, "text/html");
                // Create a multi-part to combine the parts
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);

//                message.setContent(this.content, "text/plain");
                TransportThread trans = new TransportThread();
                trans.mailSent();

            } catch (Exception e) {
                System.out.println("[ECOM] MailerNew Exception .." + e);
                e.printStackTrace();
                //  throw e;
            }
        }
    }
}
