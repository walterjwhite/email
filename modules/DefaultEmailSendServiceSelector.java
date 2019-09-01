package com.walterjwhite.email.impl;

import com.walterjwhite.email.api.annotation.EmailProvider;
import com.walterjwhite.email.api.enumeration.EmailProviderType;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailSendRequest;
import com.walterjwhite.email.api.service.EmailSendService;
import com.walterjwhite.email.api.service.EmailSendServiceSelector;
import com.walterjwhite.google.guice.implementer.AbstractSelector;
import javax.inject.Inject;
import org.reflections.Reflections;

public class DefaultEmailSendServiceSelector
    extends AbstractSelector<EmailSendRequest, EmailProviderType, EmailSendService>
    implements EmailSendServiceSelector {

  @Inject
  public DefaultEmailSendServiceSelector(Reflections reflections) {
    super(reflections, EmailSendService.class);
  }

  @Override
  public EmailSendRequest send(Email email) throws Exception {
    final EmailSendRequest emailSendRequest = new EmailSendRequest().withEmail(email);

    send(emailSendRequest);
    return emailSendRequest;
  }

  @Override
  public void send(EmailSendRequest emailSendRequest) throws Exception {
    getInstance(emailSendRequest).send(emailSendRequest);
  }

  @Override
  protected EmailProviderType get(EmailSendRequest emailSendRequest) {
    return emailSendRequest.getEmail().getFrom().getProvider().getType();
  }

  @Override
  protected boolean match(
      Class<? extends EmailSendService> serviceType, EmailSendRequest emailSendRequest) {
    return emailSendRequest
        .getEmail()
        .getFrom()
        .getProvider()
        .getType()
        .equals(((EmailProvider) serviceType.getAnnotation(EmailProvider.class)).value());
  }
}
