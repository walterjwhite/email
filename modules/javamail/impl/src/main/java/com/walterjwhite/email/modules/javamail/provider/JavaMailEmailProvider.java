package com.walterjwhite.email.modules.javamail.provider;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.modules.javamail.JavaMailMessageToEmailFunction;
import com.walterjwhite.email.organization.api.EmailProvider;
import java.util.Spliterator;
import java.util.function.Consumer;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/** Configured to read a given folder (for new messages) */
@AllArgsConstructor
@RequiredArgsConstructor
public class JavaMailEmailProvider implements EmailProvider {
  protected final transient Store store;
  protected final transient Folder folder;
  protected final int pageSize;
  protected transient int index;
  protected transient int messageCount;

  protected final transient JavaMailMessageToEmailFunction javaMailMessageToEmailFunction =
      new JavaMailMessageToEmailFunction();

  // in case we want to call split and support multiple threads
  //    public JavaMailEmailProvider(Store store, Folder folder, int pageSize, int index, int
  // messageCount) {
  //        this.store = store;
  //        this.folder = folder;
  //        this.pageSize = pageSize;
  //        this.index = index;
  //        this.messageCount = messageCount;
  //    }

  @Override
  public boolean tryAdvance(Consumer<? super Email> consumer) {
    // more efficient way to get new messages
    // folder.addMessageCountListener();
    if (index < (messageCount - 1)) {
      try {
        consumer.accept(javaMailMessageToEmailFunction.apply(folder.getMessage(index++)));
        return true;
      } catch (MessagingException e) {
        throw new RuntimeException("Error fetching message", e);
      }
    }

    return false;
  }

  // we have to tell this one we're splitting
  @Override
  public Spliterator<Email> trySplit() {
    //        if(index < messageCount)
    return null;
  }

  @Override
  public long estimateSize() {
    return messageCount;
  }

  @Override
  public int characteristics() {
    return 0;
  }

  @Override
  public void reset() {
    index = 0;

    try {
      messageCount = folder.getMessageCount();
    } catch (MessagingException e) {
      throw new RuntimeException("Unable to determine message count", e);
    }
  }
}
