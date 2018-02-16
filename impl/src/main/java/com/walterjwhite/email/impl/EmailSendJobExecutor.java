package com.walterjwhite.email.impl;

import com.sun.mail.iap.ConnectionException;
import com.walterjwhite.datastore.criteria.Repository;
import com.walterjwhite.email.api.model.EmailSendRequest;
import com.walterjwhite.email.api.service.ExternalEmailSendService;
import com.walterjwhite.queue.api.job.AbstractCallableJob;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;

public class EmailSendJobExecutor extends AbstractCallableJob<EmailSendRequest, Void> {
  protected final ExternalEmailSendService externalEmailSendService;
  protected final Provider<Repository> repositoryProvider;

  @Inject
  public EmailSendJobExecutor(
      ExternalEmailSendService externalEmailSendService, Provider<Repository> repositoryProvider) {
    this.externalEmailSendService = externalEmailSendService;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public Void call() throws Exception {
    externalEmailSendService.send(entity.getEmail());
    updateRequest();
    return null;
  }

  @Transactional
  protected void updateRequest() {
    entity.setSentDate(LocalDateTime.now());
    repositoryProvider.get().merge(entity);
  }

  @Override
  protected boolean isRetryable(Throwable thrown) {
    if (thrown.getCause() instanceof ConnectionException) return true;

    return false;
  }
}
