import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;



public class EmailSender{

    private String mas;

    public void sendMail(final String username, final String password, final String recipients) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.push-k.com.ua"); // менял
        props.put("mail.smtp.port", "26"); //менял

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));

            setSubject(message, "Тема письма"); //ТЕМА ПИСЬМА
          message.setText(mas); // ВОТ ТЕКСТ ИЛИ ТЕКСТ ИЛИ ВЛОЖЕНИЕ


            Transport.send(message);
            System.out.println("Письмо было отправлено.");

        } catch (MessagingException e) {
            System.out.println("Ошибка при отправке: " + e.toString());
        }
    }

    public void setMas(String mas) {
        this.mas = mas;
    }

    //метод для темы письма
    public static void setSubject(Message message, String subject) throws MessagingException {
        message.setSubject(subject);
    }

}


