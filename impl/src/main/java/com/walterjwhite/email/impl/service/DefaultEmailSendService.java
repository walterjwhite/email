package com.walterjwhite.email.impl.service;

import com.walterjwhite.datastore.api.repository.Repository;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailSendRequest;
import com.walterjwhite.email.api.service.EmailSendService;
import com.walterjwhite.email.impl.property.EmailSendRetryAttempts;
import com.walterjwhite.email.impl.property.EmailSendTimeout;
import com.walterjwhite.property.impl.annotation.Property;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;

// TODO: remove the dependency on the queue backend directly
// instead, create the request to send the email (database record)
// implement the queue integration by picking up "jobs" from the database record
// TODO: think about above design process?
public class DefaultEmailSendService implements EmailSendService {
  protected final Provider<Repository> repositoryProvider;

  protected final int emailSendTimeout;
  protected final int emailSendRetryAttempts;

  @Inject
  public DefaultEmailSendService(
      Provider<Repository> repositoryProvider,
      @Property(EmailSendTimeout.class) int emailSendTimeout,
      @Property(EmailSendRetryAttempts.class) int emailSendRetryAttempts) {
    this.repositoryProvider = repositoryProvider;
    this.emailSendTimeout = emailSendTimeout;
    this.emailSendRetryAttempts = emailSendRetryAttempts;
  }

  @Transactional
  @Override
  public EmailSendRequest send(Email email) {
    EmailSendRequest emailSendRequest = buildEmailSendRequest(email);

    repositoryProvider
        .get()
        .create(emailSendRequest); // TODO: differentiate between create and update

    send(emailSendRequest);
    return emailSendRequest;
  }

  protected EmailSendRequest buildEmailSendRequest(Email email) {
    EmailSendRequest emailSendRequest = new EmailSendRequest();
    emailSendRequest.setEmail(email);
    emailSendRequest.setTimeout(emailSendTimeout);
    emailSendRequest.setRetryAttempts(emailSendRetryAttempts);

    return emailSendRequest;
  }

  @Override
  public void send(EmailSendRequest emailSendRequest) {
    repositoryProvider
        .get()
        .create(emailSendRequest); // TODO: differentiate between create and update
    //    queueService.queue(buildJob(emailSendRequest));
  }
}
