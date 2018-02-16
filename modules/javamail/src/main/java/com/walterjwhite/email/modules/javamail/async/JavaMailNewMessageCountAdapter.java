package com.walterjwhite.email.modules.javamail.async;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.modules.javamail.JavaMailUtils;
import com.walterjwhite.file.api.service.FileStorageService;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMailNewMessageCountAdapter extends MessageCountAdapter {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(JavaMailNewMessageCountAdapter.class);

  protected final FileStorageService fileStorageService;

  @Inject
  public JavaMailNewMessageCountAdapter(FileStorageService fileStorageService) {
    super();
    this.fileStorageService = fileStorageService;
  }

  @Override
  public void messagesAdded(MessageCountEvent event) {
    Message[] messages = event.getMessages();

    for (Message message : messages) {
      try {
        LOGGER.debug("read message:" + message.getSubject());

        final Set<File> files = new HashSet<>();

        doStore(JavaMailUtils.doRead(message, files), files);
      } catch (Exception e) {
        LOGGER.error("Error processing email", e);
      }
    }
  }

  protected void doStore(Email email, Set<File> files) throws Exception {
    // store attachments
    for (File file : files) {
      email.getFiles().add(fileStorageService.put(file));
    }
  }
}
