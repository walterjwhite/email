package com.walterjwhite.email.impl;

import com.walterjwhite.email.api.annotation.EmailProvider;
import com.walterjwhite.email.api.enumeration.EmailProviderType;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.api.service.EmailReadService;
import com.walterjwhite.email.api.service.EmailReadServiceSelector;
import com.walterjwhite.google.guice.implementer.AbstractSelector;
import java.io.IOException;
import javax.inject.Inject;
import org.reflections.Reflections;

public class DefaultEmailReadServiceSelector
    extends AbstractSelector<PrivateEmailAccount, EmailProviderType, EmailReadService>
    implements EmailReadServiceSelector {

  @Inject
  public DefaultEmailReadServiceSelector(Reflections reflections) {
    super(reflections, EmailReadService.class);
  }

  @Override
  protected EmailProviderType get(PrivateEmailAccount privateEmailAccount) {
    return privateEmailAccount.getProvider().getType();
  }

  @Override
  protected boolean match(Class serviceType, PrivateEmailAccount privateEmailAccount) {
    return privateEmailAccount
        .getProvider()
        .getType()
        .equals(((EmailProvider) serviceType.getAnnotation(EmailProvider.class)).value());
  }

  @Override
  public void read(PrivateEmailAccount privateEmailAccount) throws IOException {
    getInstance(privateEmailAccount).read(privateEmailAccount);
  }
}
