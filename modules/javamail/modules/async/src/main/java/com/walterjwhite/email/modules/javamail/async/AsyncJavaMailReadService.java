package com.walterjwhite.email.modules.javamail.async;

import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.api.service.EmailReadService;
import com.walterjwhite.email.impl.EmailFolder;
import com.walterjwhite.property.impl.annotation.Property;
import javax.inject.Inject;
import javax.mail.event.MessageCountAdapter;

/** Monitors an inbox for new messages. */
public class AsyncJavaMailReadService implements EmailReadService, StartupAware, ShutdownAware {
  protected final ImapIdleThread imapIdleThread;

  @Inject
  public AsyncJavaMailReadService(
      MessageCountAdapter messageCountAdapter,
      PrivateEmailAccount privateEmailAccount,
      @Property(EmailFolder.class) String folderName) {
    super();
    imapIdleThread = new ImapIdleThread(messageCountAdapter, privateEmailAccount, folderName);
  }

  @Override
  public void read(PrivateEmailAccount privateEmailAccount) {
    throw (new UnsupportedOperationException());
  }

  @Override
  public void onStartUp() {
    imapIdleThread.startAsync();
  }

  @Override
  public void onShutDown() {
    imapIdleThread.stopAsync();
  }
}
