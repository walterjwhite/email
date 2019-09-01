package com.walterjwhite.email.modules.javamail;

import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.api.model.EmailEmailAccount;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.impl.EmailAddressUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.IOUtils;

/** Merely converts a message to an email object. Does not lookup DB entities ... */
public class JavaMailMessageToEmailFunction implements Function<Message, Email> {
  public static final String MESSAGE_ID_HEADER = "Message-ID";

  @Override
  public Email apply(Message message) {
    final Email email = new Email();

    try {
      addEmailContacts(message, email);
      // email.setFrom(new EmailEmailAccount(new EmailAccount(message.getFrom().toString()),
      // email,
      // EmailRecipientType.From));

      email.setFrom(getFrom(message));
      email.setMessageId(getMessageId(message));

      email.setSubject(message.getSubject());
      email.setCreatedDate(
          LocalDateTime.ofInstant(message.getReceivedDate().toInstant(), ZoneId.systemDefault()));

      if (message.isMimeType("text/plain")) {
        email.setBody(message.getContent().toString());
      } else if (message.isMimeType("multipart/*")) {
        email.setBody(getBody((MimeMultipart) message.getContent()));
      }

      handleParts(email, message);
      return email;
    } catch (MessagingException | IOException e) {
      throw new RuntimeException("Error converting message", e);
    }
  }

  protected PrivateEmailAccount getFrom(Message message) throws MessagingException {
    if (message.getFrom() == null || message.getFrom().length == 0) return null;

    return new PrivateEmailAccount(getEmailAccount(message.getFrom()[0]));
  }

  protected void addEmailContacts(final Message message, Email email) throws MessagingException {
    createEmailContacts(message, Message.RecipientType.TO, email, EmailRecipientType.To);
    createEmailContacts(message, Message.RecipientType.CC, email, EmailRecipientType.Cc);
    createEmailContacts(message, Message.RecipientType.BCC, email, EmailRecipientType.Bcc);
  }

  protected void createEmailContacts(
      final Message message,
      final Message.RecipientType recipientType,
      Email email,
      final EmailRecipientType emailRecipientType)
      throws MessagingException {

    if (message.getRecipients(recipientType) != null) {
      for (final Address address : message.getRecipients(recipientType)) {
        try {

          email
              .getEmailEmailAccounts()
              .add(new EmailEmailAccount(getEmailAccount(address), email, emailRecipientType));
        } catch (IllegalArgumentException e) {
          handleInvalidEmailAddress(e);
        }
      }
    }
  }

  protected EmailAccount getEmailAccount(final Address address) {
    final String[] emailAddressParts = EmailAddressUtil.getEmailAddressParts(address.toString());
    return new EmailAccount().withEmailAddress(emailAddressParts[1]).withName(emailAddressParts[0]);
  }

  protected void handleInvalidEmailAddress(IllegalArgumentException e) {}

  protected void handleParts(Email email, Message message) throws IOException, MessagingException {
    final Object content = message.getContent();
    if (content instanceof Multipart) {
      final Multipart multipart = (Multipart) content;
      for (int i = 0; i < multipart.getCount(); i++) {
        BodyPart bodyPart = multipart.getBodyPart(i);

        handlePart(email, bodyPart);
      }
    }
  }

  protected void handlePart(Email email, BodyPart bodyPart) throws MessagingException, IOException {
    if (isAttachment(bodyPart)) {
      handleAttachment(email, bodyPart);
    } else {
      setBody(email, bodyPart);
    }
  }

  protected void handleAttachment(Email email, BodyPart bodyPart)
      throws IOException, MessagingException {
    email.getFiles().add(getAttachment(bodyPart));
  }

  protected boolean isAttachment(BodyPart bodyPart) throws MessagingException {
    return Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
        && (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty());
  }

  protected String getBody(MimeMultipart mimeMultipart) throws MessagingException, IOException {
    StringBuilder buffer = new StringBuilder();
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      if (bodyPart.isMimeType("text/plain")) {
        buffer.append("\n" + bodyPart.getContent());
        break; // without break same text appears twice in my tests
      } else if (bodyPart.isMimeType("text/html")) {
        String html = (String) bodyPart.getContent();
        // result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
        buffer.append("\n" + html);
      } else if (bodyPart.getContent() instanceof MimeMultipart) {
        buffer.append(getBody((MimeMultipart) bodyPart.getContent()));
      }
    }

    return buffer.toString();
  }

  protected String setBody(Email email, BodyPart bodyPart) throws IOException, MessagingException {
    email.setBody(bodyPart.getContent().toString());

    if (email.getBody() == null) {
      email.setBody(IOUtils.toString(bodyPart.getInputStream(), Charset.defaultCharset()));
    }

    return email.getBody();
  }

  // FolderClosedIOException
  protected com.walterjwhite.file.api.model.File getAttachment(BodyPart bodyPart)
      throws IOException, MessagingException {
    //        try (final InputStream is = bodyPart.getInputStream()) {
    //            try (final FileEntityOutputStream feos = new
    // FileEntityOutputStream(fileStorageService)) {
    //                IOUtils.copy(is, feos);
    //                feos.flush();
    //
    //                com.walterjwhite.file.api.model.File file = feos.getFile();
    //                file.withFilename(bodyPart.getFileName());
    //
    //                return file;
    //            }
    //        }

    return null;
  }

  public static String getMessageId(Message message) throws MessagingException {
    if (message == null) return null;

    final String[] headers = message.getHeader(MESSAGE_ID_HEADER);
    if (headers == null || headers.length == 0) return null;

    return headers[0];
  }

  protected void handleTags(Folder folder, Email email) {
    if (folder != null) {
      for (final String folderName : folder.getFullName().split("/")) {
        handleTag(email, folderName);
      }
    }
  }

  protected void handleTag(Email email, final String folderName) {
    email.getTags().add(new Tag(folderName));
  }
}
