package com.walterjwhite.email.modules.javamail.organization;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.modules.javamail.AbstractJavaMail;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.email.organization.plugins.reply.MarkAsReadAction;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.search.MessageIDTerm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class JavaMailEmailMessageMarkAsRead extends AbstractJavaMail implements MarkAsReadAction {
  protected final Store store;

  @Override
  public void execute(
      final String folderName,
      PrivateEmailAccount emailAccount,
      EmailMatcherRule emailMatcherRule,
      Email email) {
    try {
      initialize(emailAccount);

      getMessage(store, folderName, email).setFlag(Flags.Flag.SEEN, true);
    } catch (MessagingException e) {
      throw new RuntimeException("Error marking message as read");
    }
  }

  public static Message getMessage(final Store store, final String folderName, final Email email) {
    try {
      final Folder folder = store.getFolder(folderName);
      folder.open(Folder.READ_ONLY);
      final Message[] messages = folder.search(new MessageIDTerm(email.getMessageId()));

      if (messages == null || messages.length == 0) {
        throw new IllegalStateException("No messages found: " + email.getMessageId());
      }
      if (messages.length > 1) {
        throw new IllegalStateException(
            "Multiples messages found: " + email.getMessageId() + " " + messages.length);
      }
      return messages[0];
    } catch (MessagingException e) {
      throw new RuntimeException("Error getting message", e);
    }
  }
}
