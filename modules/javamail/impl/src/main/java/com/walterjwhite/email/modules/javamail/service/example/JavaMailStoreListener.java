package com.walterjwhite.email.modules.javamail.service.example;

import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMailStoreListener implements StoreListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailStoreListener.class);

  @Override
  public void notification(StoreEvent e) {
    LOGGER.info("\n\nreceived event:" + e.getMessageType() + ":" + e.getMessage() + "\n\n");
  }
}
