package com.walterjwhite.email.modules.exchange;

import com.walterjwhite.property.impl.annotation.Property;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Provider;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;

public class ExchangeServiceProvider implements Provider<ExchangeService> {
  protected final ExchangeVersion exchangeVersion;
  protected final String url;
  protected final ExchangeCredentials exchangeCredentials;

  public ExchangeServiceProvider(
      @Property(com.walterjwhite.email.modules.exchange.property.ExchangeVersion.class)
          ExchangeVersion exchangeVersion,
      @Property(com.walterjwhite.email.modules.exchange.property.ExchangeUrl.class) String url,
      ExchangeCredentials exchangeCredentials) {
    this.exchangeVersion = exchangeVersion;
    this.url = url;
    this.exchangeCredentials = exchangeCredentials;
  }

  public ExchangeService get() {
    final ExchangeService exchangeService = new ExchangeService(exchangeVersion);
    try {
      exchangeService.setUrl(new URI(url));
      exchangeService.setCredentials(exchangeCredentials);
      return exchangeService;
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error building exchange service:", e);
    }
  }
}
