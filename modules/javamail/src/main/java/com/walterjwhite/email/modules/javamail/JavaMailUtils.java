package com.walterjwhite.email.modules.javamail;

import com.walterjwhite.email.api.model.Contact;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.modules.javamail.authentication.PasswordAuthenticator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;
import java.util.Set;
import javax.mail.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMailUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailUtils.class);

  private JavaMailUtils() {
    super();
  }

  private static Authenticator getAuthenticator(EmailAccount emailAccount) {
    return (new PasswordAuthenticator(
        new PasswordAuthentication(emailAccount.getUsername(), emailAccount.getPassword())));
  }

  public static Properties getProperties(EmailAccount emailAccount) {
    final Properties properties = new Properties();
    //    Properties properties = System.getProperties();

    for (final String key : emailAccount.getProvider().getSettings().keySet()) {
      properties.setProperty(key, emailAccount.getProvider().getSettings().get(key));
    }

    return (properties);
  }

  public static Session getSession(EmailAccount emailAccount) {
    return (Session.getDefaultInstance(
        getProperties(emailAccount), getAuthenticator(emailAccount)));
  }

  public static Email doRead(Message message, Set<File> files)
      throws MessagingException, IOException {
    final Email email = new Email();
    if (message.getRecipients(Message.RecipientType.TO) != null) {
      for (final Address address : message.getRecipients(Message.RecipientType.TO)) {
        email.getToRecipients().add(new Contact(address.toString()));
      }
    }

    if (message.getRecipients(Message.RecipientType.CC) != null) {
      for (final Address address : message.getRecipients(Message.RecipientType.CC)) {
        email.getCcRecipients().add(new Contact(address.toString()));
      }
    }

    if (message.getRecipients(Message.RecipientType.BCC) != null) {
      for (final Address address : message.getRecipients(Message.RecipientType.BCC)) {
        email.getBccRecipients().add(new Contact(address.toString()));
      }
    }

    email.setSubject(message.getSubject());
    email.setCreatedDate(
        LocalDateTime.ofInstant(message.getReceivedDate().toInstant(), ZoneId.systemDefault()));

    handleParts(email, message, files);

    LOGGER.debug("read email:" + email);
    LOGGER.debug("files:" + files);

    return (email);
  }

  private static void handleParts(Email email, Message message, Set<File> files)
      throws IOException, MessagingException {
    final Object content = message.getContent();
    if (content instanceof Multipart) {
      final Multipart multipart = (Multipart) content;
      for (int i = 0; i < multipart.getCount(); i++) {
        BodyPart bodyPart = multipart.getBodyPart(i);

        handleAttachmentPart(email, bodyPart, files);

        LOGGER.debug("read:" + bodyPart.getContent());
      }
    }
  }

  private static void handleAttachmentPart(Email email, BodyPart bodyPart, Set<File> files)
      throws MessagingException, IOException {
    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
        && (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {
      final File attachmentFile = getAttachment(bodyPart);
      files.add(attachmentFile);
    }
  }

  private static File getAttachment(BodyPart bodyPart) throws IOException, MessagingException {
    final File attachmentFile = File.createTempFile("email", "attachment");
    try (final InputStream is = bodyPart.getInputStream()) {
      try (final FileOutputStream fos = new FileOutputStream(attachmentFile)) {
        IOUtils.copy(is, fos);
      }
    }

    return (attachmentFile);
  }

  public static Store getStore(Session session) throws MessagingException {
    Store store = session.getStore();
    if (!store.isConnected()) {
      store.connect();
    }

    return (store);
  }
}
