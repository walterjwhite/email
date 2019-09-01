package com.walterjwhite.data.pipe.modules.email.javamail;

import com.walterjwhite.data.pipe.modules.email.javamail.sink.EmailJPASink;
import com.walterjwhite.data.pipe.modules.email.javamail.source.JavaMailFolderIterator;
import com.walterjwhite.data.pipe.modules.email.javamail.transformation.JavaMailFolderToJavaMailMessageTransformation;
import com.walterjwhite.data.pipe.modules.email.javamail.transformation.JavaMailToEmailTransformation;
import com.walterjwhite.data.pipe.modules.email.javamail.utils.StoreUtils;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.file.api.service.FileStorageService;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.MessagingException;
import javax.mail.Store;

public class JavaMailEmailReaderPipe {
  protected final Provider<EntityManager> entityManagerProvider;
  protected final FileStorageService fileStorageService;

  @Inject
  public JavaMailEmailReaderPipe(
      Provider<EntityManager> entityManagerProvider, FileStorageService fileStorageService) {
    this.entityManagerProvider = entityManagerProvider;
    this.fileStorageService = fileStorageService;
  }

  // TODO: replace manual instantiations with a reference to GuiceHelper.getInstance
  // this will allow us to use a new instance for each parallel call?
  public void run(PrivateEmailAccount emailAccount) throws MessagingException {
    final Store store = StoreUtils.getStore(emailAccount);

    // see: https://docs.oracle.com/javaee/6/api/javax/mail/Folder.html#list(java.lang.String)
    // ensure we get all folders under the root hierarchy
    StreamSupport.stream(
            new JavaMailFolderIterator(store, store.getDefaultFolder().list("*")), true)
        .flatMap(new JavaMailFolderToJavaMailMessageTransformation())
        .map(new JavaMailToEmailTransformation(fileStorageService))
        .forEach(new EmailJPASink(entityManagerProvider));
  }
}
