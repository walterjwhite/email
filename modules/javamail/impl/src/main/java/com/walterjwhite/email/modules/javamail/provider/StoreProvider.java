package com.walterjwhite.email.modules.javamail.provider;

import com.walterjwhite.email.modules.javamail.JavaMailUtils;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class StoreProvider implements Provider<Store> {
  protected final Session session;

  @Override
  public Store get() {
    try {
      return JavaMailUtils.getStore(session);
    } catch (MessagingException e) {
      throw new RuntimeException("Error getting Store", e);
    }
  }
}
