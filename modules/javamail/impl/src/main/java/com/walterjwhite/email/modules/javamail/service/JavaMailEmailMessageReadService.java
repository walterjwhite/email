package com.walterjwhite.email.modules.javamail.service;

import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.datastore.api.repository.Repository;
import com.walterjwhite.datastore.query.tag.FindTagByNameQuery;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.api.model.EmailEmailAccount;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.impl.query.FindEmailAccountByEmailAddress;
import com.walterjwhite.email.impl.query.FindPrivateEmailAccountByEmailAccount;
import com.walterjwhite.file.api.model.File;
import com.walterjwhite.file.api.service.FileEntityOutputStream;
import com.walterjwhite.file.api.service.FileStorageService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.*;
import javax.transaction.Transactional;
import org.apache.commons.io.IOUtils;

public class JavaMailEmailMessageReadService {
  protected final Provider<Repository> repositoryProvider;

  protected final FileStorageService fileStorageService;

  @Inject
  public JavaMailEmailMessageReadService(
      Provider<Repository> repositoryProvider, FileStorageService fileStorageService) {
    this.repositoryProvider = repositoryProvider;
    this.fileStorageService = fileStorageService;
  }

  @Transactional
  public Email store(Email transientEmail) throws IOException {
    transientEmail.setFrom(getFrom(transientEmail.getFrom()));

    for (final EmailEmailAccount emailEmailAccount : transientEmail.getEmailEmailAccounts()) {
      emailEmailAccount.setEmailAccount(getEmailAccount(emailEmailAccount.getEmailAccount()));
    }

    final Iterator<File> fileIterator = transientEmail.getFiles().iterator();
    while (fileIterator.hasNext()) {
      final File transientFile = fileIterator.next();
      final File persistentFile = getAttachment(transientFile);

      transientEmail.getFiles().remove(transientFile);
      transientEmail.getFiles().add(persistentFile);
    }

    final Iterator<Tag> tagIterator = transientEmail.getTags().iterator();
    while (tagIterator.hasNext()) {
      final Tag transientTag = tagIterator.next();
      final Tag persistentTag = getTag(transientTag);

      transientEmail.getTags().remove(transientTag);
      transientEmail.getTags().add(persistentTag);
    }

    return repositoryProvider
        .get()
        .create(transientEmail); // TODO: differentiate between create/update
  }

  protected PrivateEmailAccount getFrom(PrivateEmailAccount transientFromEmailAccount) {
    return repositoryProvider
        .get()
        .query(
            new FindPrivateEmailAccountByEmailAccount(
                transientFromEmailAccount.getEmailAccount()) /*,
            PersistenceOption.Create*/);
  }

  protected EmailAccount getEmailAccount(EmailAccount transientEmailAccount) {
    EmailAccount emailAccount =
        repositoryProvider
            .get()
            .query(
                new FindEmailAccountByEmailAddress(
                    transientEmailAccount.getEmailAddress()) /*, PersistenceOption.Create*/);
    if (emailAccount.getName() == null) emailAccount.setName(transientEmailAccount.getName());

    return emailAccount;
  }

  protected com.walterjwhite.file.api.model.File getAttachment(
      com.walterjwhite.file.api.model.File transientFile) throws IOException {
    try (final InputStream is = null /*transientFile.getInputStream()*/) {
      try (final FileEntityOutputStream feos = new FileEntityOutputStream(fileStorageService)) {
        IOUtils.copy(is, feos);
        feos.flush();

        com.walterjwhite.file.api.model.File file = feos.getFile();
        file.withFilename(transientFile.getName() /*bodyPart.getFileName()*/);

        return file;
      }
    }
  }

  protected Tag getTag(Tag transientTag) {
    return repositoryProvider
        .get()
        .query(new FindTagByNameQuery(transientTag.getName()) /*, PersistenceOption.Create*/);
  }
}
