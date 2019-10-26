package com.walterjwhite.email.modules.javamail.async;

import com.walterjwhite.email.modules.javamail.service.JavaMailEmailMessageReadService;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

public class JavaMailNewMessageCountAdapter extends MessageCountAdapter {

  protected final JavaMailEmailMessageReadService javaMailEmailMessageReadService;

  @Inject
  public JavaMailNewMessageCountAdapter(
      JavaMailEmailMessageReadService javaMailEmailMessageReadService) {
    this.javaMailEmailMessageReadService = javaMailEmailMessageReadService;
  }

  @Override
  public void messagesAdded(MessageCountEvent event) {
    for (Message message : event.getMessages()) {
      try {
        javaMailEmailMessageReadService.store(null, message);
      } catch (Exception e) {
        throw new RuntimeException("Error processing message", e);
      }
    }
  }
}
