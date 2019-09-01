package com.walterjwhite.email.modules.javamail.service;

import com.walterjwhite.email.api.annotation.EmailProvider;
import com.walterjwhite.email.api.enumeration.EmailProviderType;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.api.service.EmailReadService;
import java.util.Spliterator;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.mail.Folder;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads an email and stores it in a database. TODO: this is classified as a read service, it should
 * only read messages, not save them too.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
@EmailProvider(EmailProviderType.Default)
public class JavaMailRootFolderEmailSupplier implements EmailReadService {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(JavaMailRootFolderEmailSupplier.class);

  protected final JavaMailEmailMessageReadService javaMailEmailMessageReadService;

  protected final boolean recursive;
  /**
   * This is recursive
   *
   * @param privateEmailAccount
   */
  // @Override
  public void read(PrivateEmailAccount privateEmailAccount) {
    //    try (final Store store = initialize(privateEmailAccount)) {
    //      read(store.getDefaultFolder(), true);
    //    } catch (Exception e) {
    //      throw new RuntimeException("Error reading email", e);
    //    }
    //
    //    if (isRecurse(recursive, folder)) {
    //      Arrays.stream(folder.list("*")).forEach(childFolder -> read(childFolder, recursive));
    //    }
    //
    //    if(recursive){
    //      childFolders = folder.listSubscribed();//list("*");
    //    }

  }

  public static final boolean isHoldsMessages(final Folder folder) throws MessagingException {
    return (folder.getType() & Folder.HOLDS_MESSAGES) != 0;
  }

  public static final boolean isRecurse(final boolean recursive, final Folder folder)
      throws MessagingException {
    return recursive && (folder.getType() & folder.HOLDS_FOLDERS) != 0;
  }

  @Override
  public boolean tryAdvance(Consumer<? super Email> consumer) {
    return false;
  }

  @Override
  public Spliterator<Email> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return 0;
  }

  @Override
  public int characteristics() {
    return Spliterator.DISTINCT + Spliterator.NONNULL;
  }
}
