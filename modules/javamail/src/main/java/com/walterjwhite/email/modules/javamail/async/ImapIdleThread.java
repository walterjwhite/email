package com.walterjwhite.email.modules.javamail.async;

import com.google.common.util.concurrent.AbstractIdleService;
import com.sun.mail.imap.IMAPFolder;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.modules.javamail.JavaMailUtils;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImapIdleThread extends AbstractIdleService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ImapIdleThread.class);

  protected final MessageCountAdapter messageCountAdapter;
  protected final EmailAccount emailAccount;
  protected volatile Store store;
  protected volatile Folder folder;
  protected final String folderName;

  public ImapIdleThread(
      MessageCountAdapter messageCountAdapter, EmailAccount emailAccount, String folderName) {
    super();
    this.messageCountAdapter = messageCountAdapter;
    this.emailAccount = emailAccount;
    this.folderName = folderName;
  }

  @Override
  protected void startUp() throws Exception {
    store = JavaMailUtils.getStore(JavaMailUtils.getSession(emailAccount));
    folder = store.getFolder(folderName);
    folder.open(Folder.READ_ONLY);

    folder.addMessageCountListener(messageCountAdapter);
    ((IMAPFolder) folder).idle();
  }

  @Override
  protected void shutDown() throws Exception {
    closeFolder(folder);
    closeStore(store);
  }

  protected void closeStore(Store store) throws MessagingException {
    if (store != null && store.isConnected()) store.close();
  }

  protected void closeFolder(Folder folder) throws MessagingException {
    if (folder != null && folder.isOpen()) {
      folder.close(false);
    }
  }

  //    @Override
  //    protected void runOneIteration() throws Exception {
  //        try {
  //
  //            LOGGER.debug("entering idle");
  //
  //        } catch (Exception e) {
  //            LOGGER.warn("Error idling", e);
  //        }
  //    }
  //
  //    @Override
  //    protected Scheduler scheduler() {
  //        return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.SECONDS);
  //    }
}
