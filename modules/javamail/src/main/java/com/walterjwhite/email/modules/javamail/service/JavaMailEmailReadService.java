package com.walterjwhite.email.modules.javamail.service;

import com.google.inject.Inject;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.api.service.EmailReadService;
import com.walterjwhite.email.modules.javamail.JavaMailUtils;
import com.walterjwhite.file.api.service.FileStorageService;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.mail.*;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMailEmailReadService implements EmailReadService {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailEmailReadService.class);

  protected final EntityManager entityManager;

  protected final FileStorageService fileStorageService;

  @Inject
  public JavaMailEmailReadService(
      EntityManager entityManager, FileStorageService fileStorageService) {
    super();
    this.entityManager = entityManager;
    this.fileStorageService = fileStorageService;
  }

  @Override
  public void read(EmailAccount emailAccount) throws IOException {
    try {
      final Store store = JavaMailUtils.getStore(JavaMailUtils.getSession(emailAccount));

      final Folder inbox = store.getFolder("Inbox");
      inbox.open(Folder.READ_ONLY);
      for (Message message : inbox.getMessages()) {
        LOGGER.debug("read message:" + message.getSubject());
        final Set<File> files = new HashSet<>();

        doStore(JavaMailUtils.doRead(message, files), files);
      }
    } catch (NoSuchProviderException e) {
      LOGGER.error("error reading email", e);
    } catch (MessagingException e) {
      LOGGER.error("error reading email", e);
    } catch (Exception e) {
      LOGGER.error("error reading email", e);
    }
  }

  protected void doStore(Email email, Set<File> files) throws Exception {
    // store attachments
    for (File file : files) {
      email.getFiles().add(fileStorageService.put(file));
    }
  }
}
