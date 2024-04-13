import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class EmailClient {

    public static class EmailMessage {

        private String from;
        private String subject;
        private String body;

        public EmailMessage(Message message) {
            try {
                from = InternetAddress.toString(message.getFrom());
                subject = message.getSubject();
                body = message.getContent().toString();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        public String getFrom(){
            return from;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }
    }
    private static final String SMTP_HOST = "smtp.example.com";
    private static final int SMTP_PORT = 587;
    private static final String IMAP_HOST = "imap.example.com";
    private static final int IMAP_PORT = 993;
    private static final String EMAIL = "youremail@example.com";

    private static final String PASSWORD = "yourpassword";

    public static void main(String[] args) {
        EmailClient emailClient = new EmailClient();
        emailClient.sendEmail("recipient@example.com", "Test Subject", "Test message body");
        List<EmailMessage> inbox = emailClient.getInbox();
        for (EmailMessage message : inbox) {
            System.out.println("From: " + message.getFrom());
            System.out.println("Subject: " + message.getSubject());
            System.out.println("Body: " + message.getBody());
        }
    }

    public static String getSmtpHost() {
        return SMTP_HOST;
    }

    public static int getSmtpPort() {
        return SMTP_PORT;
    }

    public static String getImapHost() {
        return IMAP_HOST;
    }

    public static int getImapPort() {
        return IMAP_PORT;
    }

    public static String getEmail() {
        return EMAIL;
    }

    public static String getPassword() {
        return PASSWORD;
    }

    public void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public List<EmailMessage> getInbox() {
        Properties props = new Properties();
        props.put("mail.imap.auth", "true");
        props.put("mail.imap.starttls.enable", "true");
        props.put("mail.imap.host", IMAP_HOST);
        props.put("mail.imap.port", IMAP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Store store = session.getStore("imap");
            store.connect();

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            List<EmailMessage> emailMessages = new ArrayList<>();
            for (Message message : messages) {
                emailMessages.add(new EmailMessage(message));
            }

            inbox.close(false);
            store.close();

            return emailMessages;
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "EmailClient []";
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }
}