package com.walterjwhite.email.modules.javamail.service;

import com.walterjwhite.email.api.model.Contact;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.service.ExternalEmailSendService;
import com.walterjwhite.email.impl.MaximumAttachmentSize;
import com.walterjwhite.email.modules.javamail.JavaMailUtils;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.api.service.FileStorageService;
import com.walterjwhite.google.guice.property.property.Property;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMailEmailSendService implements ExternalEmailSendService {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailEmailSendService.class);

  protected final int maximumAttachmentSize;

  protected final FileStorageService fileStorageService;

  @Inject
  public JavaMailEmailSendService(
      @Property(MaximumAttachmentSize.class) int maximumAttachmentSize,
      FileStorageService fileStorageService) {
    super();
    this.maximumAttachmentSize = maximumAttachmentSize;
    this.fileStorageService = fileStorageService;
  }

  @Override
  public void send(Email email) throws Exception {
    final Session session = JavaMailUtils.getSession(email.getFrom());

    final Set<File> remainingAttachments = new HashSet<>();
    if (email.getFiles() != null && !email.getFiles().isEmpty())
      remainingAttachments.addAll(email.getFiles());

    int previousRemainingAttachments;
    do {
      previousRemainingAttachments = remainingAttachments.size();

      // Create a default MimeMessage object.
      MimeMessage message = new MimeMessage(session);

      // Set From: header field of the header.
      message.setFrom(new InternetAddress(email.getFrom().getName()));

      // use from to setup authentication (if required)

      addRecipients(message, email.getToRecipients(), Message.RecipientType.TO);
      addRecipients(message, email.getCcRecipients(), Message.RecipientType.CC);
      addRecipients(message, email.getBccRecipients(), Message.RecipientType.BCC);

      message.setSubject(email.getSubject());
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText(email.getBody());

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      message.setContent(multipart);

      addAttachments(multipart, email, remainingAttachments);

      // message.setText(email.getBody());

      // Send message
      Transport.send(message);
      LOGGER.debug("Sent message successfully....");
      if (previousRemainingAttachments > 0
          && previousRemainingAttachments == remainingAttachments.size()) {
        // throw(new RuntimeException("One attachment is too large"));
        LOGGER.error("One attachment is too large");
        break;
      }
    } while (remainingAttachments.size() > 0);
  }

  protected void addRecipients(
      MimeMessage message, Collection<Contact> contacts, Message.RecipientType recipientType)
      throws MessagingException {
    if (contacts != null && !contacts.isEmpty()) {
      for (final Contact contact : contacts) {
        message.addRecipient(recipientType, new InternetAddress(contact.getName()));
      }
    }
  }

  //  protected int addAttachments(Multipart multipart, Email email) throws MessagingException {
  //    return (addAttachments(multipart, email, null));
  //  }

  protected int addAttachments(Multipart multipart, Email email, Set<File> files) throws Exception {
    if (files == null || files.isEmpty()) return (0);

    final Set<File> filesAdded = new HashSet<>();

    long totalAttachmentSize = 0;
    int index = 0;
    for (File attachmentFile : files) {
      // TODO: use file storage service here
      fileStorageService.get(attachmentFile);

      final java.io.File f = new java.io.File(attachmentFile.getSource());

      if (totalAttachmentSize + f.length() > maximumAttachmentSize) {
        LOGGER.info("total attachment size:" + totalAttachmentSize + ":" + maximumAttachmentSize);
        break;
      }

      totalAttachmentSize += attachmentFile.getSource().length();
      addAttachment(multipart, f);
      index++;

      filesAdded.add(attachmentFile);
    }

    // remove this from the files we must add
    files.removeAll(filesAdded);
    return (files.size() - index);
  }

  protected void addAttachment(Multipart multipart, final java.io.File file)
      throws MessagingException {
    BodyPart attachmentPart = new MimeBodyPart();

    DataSource source = new FileDataSource(file.getAbsolutePath());
    attachmentPart.setDataHandler(new DataHandler(source));
    attachmentPart.setFileName(file.getName());

    multipart.addBodyPart(attachmentPart);
  }
}
