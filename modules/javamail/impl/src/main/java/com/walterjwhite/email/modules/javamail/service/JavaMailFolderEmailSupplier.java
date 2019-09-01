package com.walterjwhite.email.modules.javamail.service;

import com.walterjwhite.email.api.annotation.EmailProvider;
import com.walterjwhite.email.api.enumeration.EmailProviderType;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.api.service.EmailReadService;
import com.walterjwhite.email.modules.javamail.AbstractJavaMailFolderReader;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.mail.*;

/** Use Cases: 1. read all messages in a folder (excluding other folders) */
// @RequiredArgsConstructor
// (onConstructor_ = @Inject)
@EmailProvider(EmailProviderType.Default)
public class JavaMailFolderEmailSupplier extends AbstractJavaMailFolderReader
    implements EmailReadService, Supplier<Email> {
  protected transient int index;
  protected transient int messageCount;

  public JavaMailFolderEmailSupplier(PrivateEmailAccount privateEmailAccount, String folderName)
      throws MessagingException {
    super.initialize(privateEmailAccount, folderName);

    messageCount = folder.getMessageCount();
    index = 1;
  }

  @Override
  public boolean tryAdvance(Consumer<? super Email> consumer) {
    if (!hasMoreMessages()) {
      return false;
    }

    consumer.accept(doGet());
    index++;

    return hasMoreMessages();
  }

  public Email get() {
    try {
      return doGet();
    } finally {
      index++;
    }
  }

  protected Email doGet() {
    try {
      return javaMailMessageToEmailFunction.apply(folder.getMessage(index));
    } catch (MessagingException e) {
      throw new RuntimeException("Error fetching message", e);
    }
  }

  protected boolean hasMoreMessages() {
    return index <= messageCount;
  }

  @Override
  public Spliterator<Email> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return messageCount;
  }

  @Override
  public int characteristics() {
    return Spliterator.DISTINCT + Spliterator.NONNULL;
  }
}
