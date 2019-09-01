package com.walterjwhite.email.modules.exchange;

import com.walterjwhite.email.modules.exchange.property.ExchangeDomain;
import com.walterjwhite.email.modules.exchange.property.ExchangeEmailAddress;
import com.walterjwhite.email.modules.exchange.property.ExchangePassword;
import com.walterjwhite.email.modules.exchange.property.ExchangeUsername;
import com.walterjwhite.property.impl.annotation.Property;
import javax.inject.Provider;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

public class ExchangeCredentialsProvider implements Provider<ExchangeCredentials> {
  protected final String username;
  protected final String password;
  protected final String domain;
  protected final String emailAddress;

  public ExchangeCredentialsProvider(
      @Property(ExchangeUsername.class) String username,
      @Property(ExchangePassword.class) String password,
      @Property(ExchangeDomain.class) String domain,
      @Property(ExchangeEmailAddress.class) String emailAddress) {
    this.username = username;
    this.password = password;
    this.domain = domain;
    this.emailAddress = emailAddress;
  }

  @Override
  public ExchangeCredentials get() {
    return new WebCredentials(username, password, domain);
  }
}
