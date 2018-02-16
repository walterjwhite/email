package com.walterjwhite.email.modules.javamail;

import com.google.inject.AbstractModule;
import com.walterjwhite.email.api.service.EmailReadService;
import com.walterjwhite.email.api.service.ExternalEmailSendService;
import com.walterjwhite.email.modules.javamail.async.JavaMailNewMessageCountAdapter;
import com.walterjwhite.email.modules.javamail.service.JavaMailEmailReadService;
import com.walterjwhite.email.modules.javamail.service.JavaMailEmailSendService;
import javax.mail.event.MessageCountAdapter;

public class JavaMailEmailModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ExternalEmailSendService.class).to(JavaMailEmailSendService.class);
    bind(EmailReadService.class).to(JavaMailEmailReadService.class);

    bind(MessageCountAdapter.class).to(JavaMailNewMessageCountAdapter.class);
  }
}
