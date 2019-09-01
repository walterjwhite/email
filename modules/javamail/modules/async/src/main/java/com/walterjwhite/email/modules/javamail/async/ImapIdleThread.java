package com.walterjwhite.email.modules.javamail.async;

import com.sun.mail.imap.IMAPFolder;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.modules.javamail.JavaMailUtils;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;

public class ImapIdleThread implements StartupAware, ShutdownAware {
  protected final MessageCountAdapter messageCountAdapter;
  protected final PrivateEmailAccount privateEmailAccount;
  protected volatile Store store;
  protected volatile Folder folder;
  protected final String folderName;

  public ImapIdleThread(
      MessageCountAdapter messageCountAdapter,
      PrivateEmailAccount privateEmailAccount,
      String folderName) {
    super();
    this.messageCountAdapter = messageCountAdapter;
    this.privateEmailAccount = privateEmailAccount;
    this.folderName = folderName;
  }

  @Override
  public void onStartUp() throws Exception {
    store = JavaMailUtils.getStore(JavaMailUtils.getSession(privateEmailAccount));
    folder = store.getFolder(folderName);
    folder.open(Folder.READ_ONLY);

    folder.addMessageCountListener(messageCountAdapter);
    ((IMAPFolder) folder).idle();
  }

  @Override
  public void onShutDown() throws Exception {
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
