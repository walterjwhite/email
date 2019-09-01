package com.walterjwhite.data.pipe.modules.email.javamail.utils;

import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.api.model.EmailEmailAccount;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.impl.EmailAddressUtil;
import java.util.HashSet;
import java.util.Set;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

public class ContactUtils {
  private ContactUtils() {}

  public static PrivateEmailAccount getFrom(Message message) throws MessagingException {
    if (message.getFrom() == null || message.getFrom().length == 0) return null;

    final EmailAccount emailAccount = getEmailAccount(message.getFrom()[0]);
    if (emailAccount != null) return new PrivateEmailAccount(emailAccount);

    return null;
  }

  private static EmailAccount getEmailAccount(final Address address) {
    final String[] emailAddressParts = EmailAddressUtil.getEmailAddressParts(address.toString());

    EmailAccount emailAccount = new EmailAccount();
    emailAccount.setName(emailAddressParts[0]);
    emailAccount.setEmailAddress(emailAddressParts[1]);

    return emailAccount;
  }

  public static Set<EmailEmailAccount> getEmailAccounts(Message message, Email email)
      throws MessagingException {
    final Set<EmailEmailAccount> emailAccounts = new HashSet<>();

    for (EmailRecipientType emailRecipientType : EmailRecipientType.values()) {
      final Message.RecipientType recipientType = getRecipientType(emailRecipientType);

      if (recipientType != null) {
        if (message.getRecipients(recipientType) != null) {
          for (final Address address : message.getRecipients(recipientType)) {
            emailAccounts.add(
                new EmailEmailAccount(getEmailAccount(address), email, emailRecipientType));
          }
        }
      }
    }

    return emailAccounts;
  }

  public static Message.RecipientType getRecipientType(EmailRecipientType emailRecipientType) {
    if (emailRecipientType.equals(EmailRecipientType.Bcc)) return Message.RecipientType.BCC;
    if (emailRecipientType.equals(EmailRecipientType.Cc)) return Message.RecipientType.CC;
    if (emailRecipientType.equals(EmailRecipientType.To)) return Message.RecipientType.TO;
    // if (emailRecipientType.equals(EmailRecipientType.From)) return null;

    throw new IllegalStateException("Unknown:" + emailRecipientType);
  }
}
