package com.walterjwhite.email.modules.javamail.async;

import com.google.common.util.concurrent.AbstractIdleService;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.api.service.EmailReadService;
import com.walterjwhite.email.impl.EmailFolder;
import com.walterjwhite.google.guice.property.property.Property;
import java.io.IOException;
import javax.inject.Inject;
import javax.mail.event.MessageCountAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Monitors an inbox for new messages. */
public class AsyncJavaMailReadService extends AbstractIdleService implements EmailReadService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncJavaMailReadService.class);

  protected final ImapIdleThread imapIdleThread;

  @Inject
  public AsyncJavaMailReadService(
      MessageCountAdapter messageCountAdapter,
      EmailAccount emailAccount,
      @Property(EmailFolder.class) String folderName) {
    super();
    imapIdleThread = new ImapIdleThread(messageCountAdapter, emailAccount, folderName);
  }

  @Override
  public void read(EmailAccount emailAccount) throws IOException {
    throw (new UnsupportedOperationException());
  }

  @Override
  protected void startUp() throws Exception {
    imapIdleThread.startAsync();
  }

  @Override
  protected void shutDown() throws Exception {
    imapIdleThread.stopAsync();
  }
}
