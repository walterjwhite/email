package com.walterjwhite.email.modules.javamail.organization;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.modules.javamail.AbstractJavaMail;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.email.organization.plugins.reply.MoveAction;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public class JavaMailEmailMessageMover extends AbstractJavaMail implements MoveAction {
  @Override
  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      EmailMatcherRule emailMatcherRule,
      Email email) {
    try {
      initialize(privateEmailAccount);
    } catch (MessagingException e) {
      throw new RuntimeException("Error initializing javamail email mover", e);
    }

    final Message message = JavaMailEmailMessageMarkAsRead.getMessage(store, folderName, email);

    try (final Folder source = message.getFolder()) {
      if (source.isOpen()) {
        source.close();
      }

      source.open(Folder.READ_WRITE);

      try (final Folder target = mkdir(privateEmailAccount, getTargetFolderId(emailMatcherRule))) {
        target.open(Folder.READ_WRITE);

        copy(message, source, target);
        delete(message, source);
      }
    } catch (MessagingException e) {
      throw new RuntimeException("Error moving email", e);
    }
  }

  protected void copy(final Message message, final Folder source, final Folder target)
      throws MessagingException {
    source.copyMessages(new Message[] {message}, target);
  }

  protected void delete(final Message message, final Folder source) throws MessagingException {
    message.setFlag(Flags.Flag.DELETED, true);
    source.expunge();
  }

  protected Folder mkdir(PrivateEmailAccount emailAccount, final String path)
      throws MessagingException {
    final Folder folder = store.getFolder(path);
    if (!folder.exists()) {
      folder.create(emailAccount.getFolderType());
      folder.setSubscribed(true);
    }

    return folder;
  }
}
