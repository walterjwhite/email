package com.walterjwhite.email.modules.template.service;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.datastore.api.repository.Repository;
import com.walterjwhite.datastore.query.entityReference.FindEntityReferenceByTypeAndIdQuery;
import com.walterjwhite.datastore.query.entityType.FindEntityTypeByNameQuery;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.api.model.EmailEmailAccount;
import com.walterjwhite.email.modules.template.model.EmailTemplate;
import com.walterjwhite.email.modules.template.model.EmailTemplateContact;
import com.walterjwhite.email.modules.template.model.EmailTemplateEmailRecipientProviderConfiguration;
import com.walterjwhite.email.modules.template.model.EmailTemplateSendRequest;
import com.walterjwhite.template.api.service.TemplateService;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;

public class DefaultEmailTemplateService implements EmailTemplateService {
  protected final TemplateService templateService;

  protected final Provider<Repository> repositoryProvider;

  @Inject
  public DefaultEmailTemplateService(
      TemplateService templateService, Provider<Repository> repositoryProvider) {
    this.templateService = templateService;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public Set<EmailTemplateSendRequest> render(
      EmailTemplate emailTemplate, AbstractEntity... referenceDatas) {
    final Set<EmailTemplateSendRequest> emailTemplateSendRequests = new HashSet<>();

    for (AbstractEntity referenceData : referenceDatas) {
      emailTemplateSendRequests.add(render(emailTemplate, referenceData));
    }

    return emailTemplateSendRequests;
  }

  // TODO: automatically create
  protected EmailTemplateSendRequest render(
      EmailTemplate emailTemplate, AbstractEntity referenceData) {
    Email email = new Email();

    email.setSubject(templateService.apply(emailTemplate.getSubjectTemplate(), referenceData));
    email.setBody(templateService.apply(emailTemplate.getBodyTemplate(), referenceData));

    // TODO: integrate attachments / file support

    addStaticRecipients(emailTemplate, email);
    updateRecipients(emailTemplate, email, referenceData);

    final Repository repository = repositoryProvider.get();

    return (EmailTemplateSendRequest)
        new EmailTemplateSendRequest(
                emailTemplate,
                repository.query(
                    new FindEntityReferenceByTypeAndIdQuery(
                        repository.query(
                            new FindEntityTypeByNameQuery(referenceData.getClass().getName())),
                        referenceData.getId()) /*,
                        PersistenceOption.Create*/))
            .withEmail(email);
  }

  protected void addStaticRecipients(EmailTemplate emailTemplate, Email email) {
    if (emailTemplate.getEmailTemplateContacts() != null
        && !emailTemplate.getEmailTemplateContacts().isEmpty()) {
      for (EmailTemplateContact emailTemplateContact : emailTemplate.getEmailTemplateContacts()) {
        email
            .getEmailEmailAccounts()
            .add(
                new EmailEmailAccount(
                    emailTemplateContact.getEmailAccount(),
                    email,
                    emailTemplateContact.getEmailRecipientType()));
      }
    }
  }

  protected void updateRecipients(
      EmailTemplate emailTemplate, Email email, Object... referenceData) {
    // get recipients (dynamic list)
    for (EmailTemplateEmailRecipientProviderConfiguration
        emailTemplateEmailRecipientProviderConfiguration :
            emailTemplate.getEmailTemplateEmailRecipientProviderConfigurations()) {
      final Set<EmailAccount> contactsToAdd =
          getContacts(emailTemplateEmailRecipientProviderConfiguration, referenceData);

      if (contactsToAdd != null && !contactsToAdd.isEmpty()) {
        for (EmailAccount emailAccount : contactsToAdd)
          email
              .getEmailEmailAccounts()
              .add(
                  new EmailEmailAccount(
                      emailAccount,
                      email,
                      emailTemplateEmailRecipientProviderConfiguration.getEmailRecipientType()));
      }
    }
  }

  // TODO: implement
  protected Set<EmailAccount> getContacts(
      EmailTemplateEmailRecipientProviderConfiguration
          emailTemplateEmailRecipientProviderConfiguration,
      Object... referenceData) {
    //    try {
    //      EmailRecipientProvider emailRecipientProvider =
    //          (EmailRecipientProvider)
    //              GuiceHelper.getGuiceApplicationInjector()
    //                  .getInstance(
    //                      Class.forName(
    //                          emailTemplateEmailRecipientProviderConfiguration
    //                              .getEmailRecipientProviderConfiguration()
    //                              .getName()));
    //      return emailRecipientProvider.getRecipients(referenceData);
    return null;
    //    } catch (ClassNotFoundException e) {
    //      throw new RuntimeException(e));
    //    }
  }
}
