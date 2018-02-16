package com.walterjwhite.email.impl;

import com.walterjwhite.datastore.criteria.Repository;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailSendRequest;
import com.walterjwhite.email.api.service.EmailSendService;
import com.walterjwhite.google.guice.property.property.Property;
import com.walterjwhite.job.api.model.Job;
import com.walterjwhite.job.impl.JobBuilder;
import com.walterjwhite.queue.api.service.QueueService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;

public class DefaultEmailSendService implements EmailSendService {
  protected final QueueService queueService;
  protected final JobBuilder jobBuilder;
  protected final Provider<Repository> repositoryProvider;

  protected final int emailSendTimeout;
  protected final int emailSendRetryAttempts;

  @Inject
  public DefaultEmailSendService(
      QueueService queueService,
      JobBuilder jobBuilder,
      Provider<Repository> repositoryProvider,
      @Property(EmailSendTimeout.class) int emailSendTimeout,
      @Property(EmailSendRetryAttempts.class) int emailSendRetryAttempts) {
    this.queueService = queueService;
    this.jobBuilder = jobBuilder;
    this.repositoryProvider = repositoryProvider;
    this.emailSendTimeout = emailSendTimeout;
    this.emailSendRetryAttempts = emailSendRetryAttempts;
  }

  @Transactional
  @Override
  public EmailSendRequest send(Email email) throws Exception {
    EmailSendRequest emailSendRequest = buildEmailSendRequest(email);

    repositoryProvider.get().merge(emailSendRequest);

    send(emailSendRequest);
    //    emailSendRequest.setSentDate(LocalDateTime.now());
    //    repositoryProvider.get().merge(emailSendRequest);

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
  public void send(EmailSendRequest emailSendRequest) throws Exception {
    queueService.queue(buildJob(emailSendRequest));
  }

  protected Job buildJob(EmailSendRequest emailSendRequest) {
    Job job = jobBuilder.build(emailSendRequest);

    job.setExecutionTimeLimit((int) emailSendRequest.getTimeout());
    job.setRetryAttempts(emailSendRequest.getRetryAttempts());
    job.setExecutionTimeLimitUnits(TimeUnit.SECONDS);
    job.setSynchronous(emailSendRequest.isSynchronous());

    return job;
  }
}
