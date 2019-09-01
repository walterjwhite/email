package com.walterjwhite.email.modules.javamail.provider;

import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.modules.javamail.JavaMailUtils;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.Session;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SessionProvider implements Provider<Session> {
  protected final PrivateEmailAccount privateEmailAccount;

  @Override
  public Session get() {
    return JavaMailUtils.getSession(privateEmailAccount);
  }
}
