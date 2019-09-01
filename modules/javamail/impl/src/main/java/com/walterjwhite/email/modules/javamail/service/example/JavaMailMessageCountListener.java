package com.walterjwhite.email.modules.javamail.service.example;

import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMailMessageCountListener implements MessageCountListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailMessageCountListener.class);

  @Override
  public void messagesAdded(MessageCountEvent e) {
    LOGGER.info("messagesAdded:" + e.getType());
  }

  @Override
  public void messagesRemoved(MessageCountEvent e) {
    LOGGER.info("messagesRemoved:" + e.getType());
  }
}
