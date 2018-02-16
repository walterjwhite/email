package com.walterjwhite.email.impl;

import com.google.inject.AbstractModule;
import com.walterjwhite.email.api.service.EmailSendService;

public class EmailModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EmailSendService.class).to(DefaultEmailSendService.class);
  }
}
