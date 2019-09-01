package com.walterjwhite.email.modules.exchange.organization;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.email.organization.plugins.reply.MarkAsReadAction;
import lombok.RequiredArgsConstructor;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.ItemId;

@RequiredArgsConstructor
public class ExchangeMarkMessageAsRead implements MarkAsReadAction {
  protected final ExchangeService exchangeService;

  @Override
  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      EmailMatcherRule emailMatcherRule,
      Email email) {
    try {
      final ItemId itemId = new ItemId(email.getMessageId());
      final EmailMessage emailMessage = EmailMessage.bind(exchangeService, itemId);
      emailMessage.setIsRead(true);
      emailMessage.update(ConflictResolutionMode.AutoResolve);
    } catch (Exception e) {
      throw new RuntimeException("Error marking email as read", e);
    }
  }
}
