package com.walterjwhite.data.pipe.modules.email.javamail.source;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

public class JavaMailFolderIterator extends Spliterators.AbstractSpliterator<Folder>
    implements AutoCloseable {
  protected transient Store store;
  protected final Folder[] folders;
  protected AtomicInteger index = new AtomicInteger(0);

  public JavaMailFolderIterator(Store store, Folder[] folders) throws MessagingException {
    super(
        folders.length,
        Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.NONNULL);
    this.store = store;

    this.folders = folders;
    filterFolders();
  }

  protected void filterFolders() throws MessagingException {
    for (int i = 0; i < folders.length; i++) {
      if ((folders[i].getType() & Folder.HOLDS_MESSAGES) == 0) {
        folders[i] = null;
      }
    }
  }

  @Override
  public void close() throws Exception {
    store.close();
  }

  @Override
  public boolean tryAdvance(Consumer<? super Folder> consumer) {
    if (folders == null) return false;

    final int nextIndex = index.getAndIncrement();
    if (nextIndex < folders.length) {
      if (folders[nextIndex] != null) {
        consumer.accept(folders[nextIndex]);
        return true;
      }
    }

    return false;
  }
}
