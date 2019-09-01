package com.walterjwhite.email.modules.javamail;

import com.walterjwhite.email.api.model.PrivateEmailAccount;
import javax.mail.Folder;
import javax.mail.MessagingException;

// @RequiredArgsConstructor
public abstract class AbstractJavaMailFolderReader extends AbstractJavaMail {
  protected transient String folderName;
  protected transient Folder folder;
  protected final transient JavaMailMessageToEmailFunction javaMailMessageToEmailFunction =
      new JavaMailMessageToEmailFunction();

  public void initialize(PrivateEmailAccount privateEmailAccount, String folderName)
      throws MessagingException {
    super.initialize(privateEmailAccount);

    this.folderName = folderName;

    folder = getFolder();
    folder.open(Folder.READ_ONLY);
  }

  protected Folder getFolder() throws MessagingException {
    if (folderName == null) return store.getDefaultFolder();

    return store.getFolder(folderName);
  }

  @Override
  public void close() {
    closeFolder();

    super.close();
  }

  protected void closeFolder() {
    if (folder != null) {

      try {
        // close all child folders too
        folder.close();
      } catch (MessagingException e) {
        onMessagingException(e);
      }
    }
  }
}
