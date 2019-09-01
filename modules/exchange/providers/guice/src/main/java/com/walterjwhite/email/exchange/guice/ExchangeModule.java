package com.walterjwhite.email.exchange.guice;

import com.google.inject.AbstractModule;

public class ExchangeModule extends AbstractModule {
  @Override
  protected void configure() {
    super.configure();

    //    bind(ExchangeCredentials.class).toProvider(ExchangeCredentialsProvider.class);
    //    bind(ExchangeService.class).toProvider(ExchangeServiceProvider.class);
    //    bind(EmailService.class).to(DefaultExchangeEmailService.class);
  }
}
