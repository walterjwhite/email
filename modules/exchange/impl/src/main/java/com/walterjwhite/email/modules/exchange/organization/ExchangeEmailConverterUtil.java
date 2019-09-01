package com.walterjwhite.email.modules.exchange.organization;

import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import com.walterjwhite.email.api.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeEmailConverterUtil {
  public static Email build(EmailMessage emailMessage) throws ServiceLocalException {
    final Email email = new Email();
    email.setMessageId(emailMessage.getId().getUniqueId());

    // emailMessage.getInternetMessageId(),;
    email.setConversation(new EmailConversation(emailMessage.getConversationId().getUniqueId()));

    email.setSubject(emailMessage.getSubject());
    email.setBody(emailMessage.getBody().toString());

    // addEmailAddress(email, emailMessage.getFrom().getAddress(), EmailRecipientType.);
    email.setFrom(
        new PrivateEmailAccount(
            new EmailAccount().withEmailAddress(emailMessage.getFrom().getAddress())));

    emailMessage
        .getToRecipients()
        .spliterator()
        .forEachRemaining(
            emailAddress ->
                addEmailAddress(email, emailAddress.getAddress(), EmailRecipientType.To));
    emailMessage
        .getCcRecipients()
        .spliterator()
        .forEachRemaining(
            emailAddress ->
                addEmailAddress(email, emailAddress.getAddress(), EmailRecipientType.Cc));
    emailMessage
        .getBccRecipients()
        .spliterator()
        .forEachRemaining(
            emailAddress ->
                addEmailAddress(email, emailAddress.getAddress(), EmailRecipientType.Bcc));

    return email;
  }

  private static void addEmailAddress(
      final Email email, final String emailAddress, final EmailRecipientType emailRecipientType) {
    if (emailAddress == null || emailAddress.isEmpty()) return;

    email
        .getEmailEmailAccounts()
        .add(
            new EmailEmailAccount(
                new EmailAccount().withEmailAddress(emailAddress), email, emailRecipientType));
  }
}
