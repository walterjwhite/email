package com.walterjwhite.email.modules.javamail.service;

import com.sun.mail.imap.IMAPFolder;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.api.service.NewEmailSupplier;
import com.walterjwhite.email.modules.javamail.AbstractJavaMailFolderReader;
import com.walterjwhite.email.modules.javamail.service.example.Listener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import javax.mail.MessagingException;

// @EmailProvider(EmailProviderType.Default)
public class JavaMailNewEmailSupplier extends AbstractJavaMailFolderReader
    implements NewEmailSupplier {
  // TODO: make this configurable
  protected transient BlockingQueue<Email> messageCountEventBlockingQueue;
  protected transient JavaMailMessageCountListenerQueue javaMailMessageCountListenerQueue;
  protected transient Listener listener;

  public JavaMailNewEmailSupplier(PrivateEmailAccount privateEmailAccount, String folderName)
      throws MessagingException {
    initialize(privateEmailAccount, folderName);
    initialize();
  }

  private void initialize() {
    messageCountEventBlockingQueue =
        new PriorityBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 2);
    javaMailMessageCountListenerQueue =
        new JavaMailMessageCountListenerQueue(messageCountEventBlockingQueue);
    listener = new Listener();

    folder.addMessageCountListener(javaMailMessageCountListenerQueue);

    listener.start((IMAPFolder) folder);
  }

  @Override
  public Email get() {
    try {
      return messageCountEventBlockingQueue.take();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();

      throw new IllegalStateException("Interrupted", e);
    }
  }
}
