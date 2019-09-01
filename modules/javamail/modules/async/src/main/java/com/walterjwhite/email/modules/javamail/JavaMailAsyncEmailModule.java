package com.walterjwhite.email.modules.javamail;

import com.google.inject.AbstractModule;
import com.walterjwhite.email.modules.javamail.async.JavaMailNewMessageCountAdapter;
import javax.mail.event.MessageCountAdapter;

public class JavaMailAsyncEmailModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(MessageCountAdapter.class).to(JavaMailNewMessageCountAdapter.class);

    install(new JavaMailEmailModule());
  }
}
