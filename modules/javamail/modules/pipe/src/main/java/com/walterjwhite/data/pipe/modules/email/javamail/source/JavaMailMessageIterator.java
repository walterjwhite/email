package com.walterjwhite.data.pipe.modules.email.javamail.source;

import com.walterjwhite.data.pipe.modules.email.javamail.model.JavaMailMessage;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.mail.Folder;
import javax.mail.MessagingException;

public class JavaMailMessageIterator extends Spliterators.AbstractSpliterator<JavaMailMessage> {
  protected final Folder folder;
  protected AtomicInteger index = new AtomicInteger(1);
  protected final int messageCount;

  public JavaMailMessageIterator(Folder folder) throws MessagingException {
    super(
        folder.getMessageCount(),
        Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.NONNULL);

    this.folder = folder;
    this.messageCount = folder.getMessageCount();
  }

  @Override
  public boolean tryAdvance(Consumer<? super JavaMailMessage> consumer) {
    if (index.get() <= messageCount) {
      try {
        consumer.accept(new JavaMailMessage(folder.getMessage(index.getAndIncrement()), folder));
        return true;
      } catch (MessagingException e) {
        throw new RuntimeException("Error processing message in folder", e);
      }
    }

    return false;
  }
}
