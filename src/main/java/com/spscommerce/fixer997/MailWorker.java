package com.spscommerce.fixer997;

import com.spscommerce.fixer997.config.Config;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by ikornienko on 8/11/2017.
 */
public class MailWorker implements AutoCloseable {
    public static Logger log = Logger.getLogger(MailWorker.class); // logger initialization
    public static SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    private Config config;
    private Session session;
    private Store store;

    public MailWorker(Config conf) {
        config = conf;
        connect();
    }

    private void connect() {
        connectToServer();
        connectToImapsStore();
    }

    private void connectToServer() {
        session = Session.getInstance(config.getProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getLogin(), config.getPassword());
                    }
                });
    }

    private void connectToImapsStore() {
        try {
            store = session.getStore("imaps");
            store.connect(config.getImapHost(), config.getLogin(), config.getPassword());
            log.info("We are connected to the server");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            log.error("NoSuchProviderException in MailWorker class happened\n" + e);
        } catch (MessagingException e) {
            log.error("MessagingException in MailWorker class happened (connectToImapsStore method)\n" + e);
            e.printStackTrace();
        }
    }

    public void emailSearch(String nameOfFile, String dateOfFile, String server) {
        try {
            Folder emailFolder = store.getFolder(config.getEmailFolder());
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = getActualMessages(emailFolder, dateOfFile);
            for (int i = messages.length - 1; i >= 0; i--) {
                Message currentMessage = messages[i];
                Date letterDate = currentMessage.getReceivedDate();
                String dateOfMail = newDateFormat.format(letterDate);
                long dateOfMaleInt = Long.parseLong(dateOfMail);
                long dateOfFileInt = Long.parseLong(dateOfFile);
                if (Long.parseLong(dateOfMail.substring(0, 8)) == Long.parseLong(dateOfFile.substring(0, 8))) {
                    String subject = currentMessage.getSubject();
                    if (server.matches("EDI2") && subject.equals("RE: Translator Server Notification: EDI X12 from ICC FS IN")
                            && (dateAnalyzer(dateOfMaleInt, dateOfFileInt, config.getEmailFault()))) {
                        if (sendEdi2Email(nameOfFile, currentMessage, subject)) break;
                    } else if (server.matches("EDI1") && subject.equals("RE: Download from ICC has been finished with errors (warnings)")
                            && (dateAnalyzer(dateOfMaleInt, dateOfFileInt, config.getEmailFault()))) {
                        if (sendEdi1Email(nameOfFile, currentMessage, subject)) break;
                    }
                }
                if (Long.parseLong(dateOfMail.substring(0, 8)) < Long.parseLong(dateOfFile.substring(0, 8))) {
                    log.error("We didn't find right letter. Who knows why...");
                    break;
                }
            }
            emailFolder.close(false);
        } catch (MessagingException e) {
            log.error("MessagingException in MailWorker class happened (emailSearch method)\n" + e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IOException in MailWorker class happened\n" + e);
            e.printStackTrace();
        }

    }

    private boolean sendEdi1Email(String nameOfFile, Message currentMessage, String subject) throws IOException, MessagingException {
        String contentHere = (String) currentMessage.getContent();
        if (contentHere.contains("Object reference not set to an instance of an object. at EDIFilesInterchange")) {
            log.info("We have found right letter");
            replyEmail(session, subject, currentMessage.getContent(), nameOfFile);
            log.info("We have replied letter");
            return true;
        }
        return false;
    }

    private boolean sendEdi2Email(String nameOfFile, Message currentMessage, String subject) throws IOException, MessagingException {
        String contentHere = (String) currentMessage.getContent();
        if (contentHere.contains("TA1") && contentHere.contains(nameOfFile)) {
            log.info("We have found right letter");
            replyEmail(session, subject, currentMessage.getContent(), nameOfFile);
            log.info("We have replied letter");
            return true;
        }
        return false;
    }

    public void replyEmail(Session session, String subject, Object content, String nameOfFile) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getLogin()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.getToRecipient()));
            if (config.getCcRecipient() != null)
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(config.getCcRecipient()));
            message.setSubject("RE: " + subject);
            content = "File: " + nameOfFile + " has been fixed and resent to production folder\n\n" + content;
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (AddressException e) {
            log.error("AddressException in MailWorker class happened\n" + e);
            e.printStackTrace();
        } catch (MessagingException e) {
            log.error("MessagingException in MailWorker class happened (replyEmail method)\n" + e);
            e.printStackTrace();
        }
    }

    public boolean dateAnalyzer(Long dateOfMale, Long dateOfFile, int delta) {
        if (dateOfMale > dateOfFile + delta || dateOfMale > dateOfFile - delta) {
            return true;
        } else {
            return false;
        }
    }

    public Message[] getActualMessages(Folder folder, String dateOfFile) {
        int multiplier = 1;
        Message[] messages = null;
        boolean isDateCorrect = true;
        try {
            while (isDateCorrect) {
                if ((folder.getMessageCount() > config.getMinMessageCount() * multiplier) && isDateCorrect) {
                    messages = folder.getMessages(folder.getMessageCount() - (config.getMinMessageCount() * multiplier), folder.getMessageCount());
                    String dateOfMail = newDateFormat.format(messages[0].getReceivedDate());
                    multiplier++;
                    if (Long.parseLong(dateOfMail.substring(0, 8)) < Long.parseLong(dateOfFile.substring(0, 8))) {
                        isDateCorrect = false;
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void close() throws Exception {
        store.close();
    }

}
