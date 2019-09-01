package com.walterjwhite.data.pipe.modules.email.javamail.transformation;

import com.walterjwhite.data.pipe.modules.email.javamail.model.JavaMailMessage;
import com.walterjwhite.data.pipe.modules.email.javamail.utils.AttachmentUtils;
import com.walterjwhite.data.pipe.modules.email.javamail.utils.ContactUtils;
import com.walterjwhite.data.pipe.modules.email.javamail.utils.MessageUtils;
import com.walterjwhite.data.pipe.modules.email.javamail.utils.TagUtils;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.file.api.service.FileStorageService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;
import javax.mail.*;

public class JavaMailToEmailTransformation implements Function<JavaMailMessage, Email> {
  protected final FileStorageService fileStorageService;

  public JavaMailToEmailTransformation(FileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @Override
  public Email apply(JavaMailMessage javaMailMessage) {
    try {
      final Email email = new Email();

      addEmailContacts(javaMailMessage.getMessage(), email);
      email.setFrom(ContactUtils.getFrom(javaMailMessage.getMessage()));
      email.setServerId(MessageUtils.getServerId(javaMailMessage.getMessage()));

      email.setSubject(javaMailMessage.getMessage().getSubject());
      email.setCreatedDate(
          LocalDateTime.ofInstant(
              javaMailMessage.getMessage().getReceivedDate().toInstant(), ZoneId.systemDefault()));

      email.setBody(MessageUtils.getBody(javaMailMessage.getMessage()));

      handleParts(email, javaMailMessage.getMessage());

      doTags(javaMailMessage.getFolder(), email);
      return email;
    } catch (IOException | MessagingException e) {
      throw new RuntimeException("Error transforming", e);
    }
  }

  protected void handleParts(Email email, Message message) throws IOException, MessagingException {
    final Object content = message.getContent();
    if (content instanceof Multipart) {
      final Multipart multipart = (Multipart) content;
      for (int i = 0; i < multipart.getCount(); i++) {
        handlePart(email, multipart.getBodyPart(i));
      }
    }
  }

  protected void handlePart(Email email, BodyPart bodyPart) throws MessagingException, IOException {
    if (AttachmentUtils.isAttachment(bodyPart)) {
      handleAttachment(email, bodyPart);
    } else {
      setBody(email, bodyPart);
    }
  }

  protected void handleAttachment(Email email, BodyPart bodyPart)
      throws IOException, MessagingException {
    email.getFiles().add(AttachmentUtils.getAttachment(fileStorageService, bodyPart));
  }

  protected void setBody(Email email, BodyPart bodyPart) throws IOException, MessagingException {
    MessageUtils.getBody(bodyPart);
  }

  protected void addEmailContacts(final Message message, Email email) throws MessagingException {
    email.getEmailEmailAccounts().addAll(ContactUtils.getEmailAccounts(message, email));
  }

  protected void doTags(Folder folder, Email email) {
    email.getTags().addAll(TagUtils.get(folder, email));
  }
}
