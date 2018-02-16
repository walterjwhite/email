package com.walterjwhite.email.modules.javamail.provider;

import com.walterjwhite.email.api.model.EmailAccount;
import javax.inject.Provider;

/**
 * TODO: implement this such that it will look for email accounts configured for polling and provide
 * those expect to return more than 1 instance.
 */
public class EmailAccountProvider implements Provider<EmailAccount> {
  @Override
  public EmailAccount get() {
    return null;
  }
}
