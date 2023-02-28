package app.services;

import app.model.Order;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

@Service("emailService")
public class EmailService
{
    private static final String username = "chipdip.real@outlook.com";
    private static final String password = "chipdip1_";

    public void sendConfirmation(Order order)
    {
        String content = "ЗАО «ЧИП и ДИП» — Приборы, Радиодетали и Электронные компоненты\n\n";
        content += "Ваш заказ принят!\n";
        content += "Не забудьте, что заказ необходимо оплатить в течение 20 секунд!";

        send(order.getClientEmail(), "Заказ принят!", content);
    }

    private void send(String recipient, String subject, String content)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");

        try
        {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}