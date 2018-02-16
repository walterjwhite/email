package com.walterjwhite.email.modules.template.service;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import com.walterjwhite.email.api.model.Contact;
import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.modules.template.model.EmailTemplate;
import com.walterjwhite.email.modules.template.model.EmailTemplateEmailRecipientProviderConfiguration;
import com.walterjwhite.email.modules.template.model.EmailTemplateSendRequest;
import com.walterjwhite.template.api.service.TemplateService;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;

public class DefaultEmailTemplateService implements EmailTemplateService {
  protected final TemplateService templateService;

  @Inject
  public DefaultEmailTemplateService(TemplateService templateService) {
    this.templateService = templateService;
  }

  protected Email render(EmailTemplate emailTemplate, Object... referenceData) {
    Email email = new Email();

    // TODO: support attaching files (will not be part of a template, right?)
    email.setSubject(templateService.apply(emailTemplate.getSubjectTemplate(), referenceData));
    email.setBody(templateService.apply(emailTemplate.getBodyTemplate(), referenceData));

    addStaticRecipients(emailTemplate, email);
    updateRecipients(emailTemplate, email);

    return email;
  }

  protected void addStaticRecipients(EmailTemplate emailTemplate, Email email) {
    if (emailTemplate.getToRecipients() != null && !emailTemplate.getToRecipients().isEmpty())
      email.getToRecipients().addAll(emailTemplate.getToRecipients());
    if (emailTemplate.getCcRecipients() != null && !emailTemplate.getCcRecipients().isEmpty())
      email.getCcRecipients().addAll(emailTemplate.getCcRecipients());
    if (emailTemplate.getBccRecipients() != null && !emailTemplate.getBccRecipients().isEmpty())
      email.getBccRecipients().addAll(emailTemplate.getBccRecipients());
  }

  protected void updateRecipients(EmailTemplate emailTemplate, Email email) {
    // get recipients (dynamic list)
    for (EmailTemplateEmailRecipientProviderConfiguration
        emailTemplateEmailRecipientProviderConfiguration :
            emailTemplate.getEmailTemplateEmailRecipientProviderConfigurations()) {
      if (EmailRecipientType.To.equals(
          emailTemplateEmailRecipientProviderConfiguration.getRecipientType())) {
        email
            .getToRecipients()
            .addAll(getContacts(emailTemplateEmailRecipientProviderConfiguration));
      } else if (EmailRecipientType.Cc.equals(
          emailTemplateEmailRecipientProviderConfiguration.getRecipientType())) {
        email
            .getCcRecipients()
            .addAll(getContacts(emailTemplateEmailRecipientProviderConfiguration));
      }
      if (EmailRecipientType.Bcc.equals(
          emailTemplateEmailRecipientProviderConfiguration.getRecipientType())) {
        email
            .getBccRecipients()
            .addAll(getContacts(emailTemplateEmailRecipientProviderConfiguration));
      } else {
        throw new IllegalStateException(
            "value is not supported:"
                + emailTemplateEmailRecipientProviderConfiguration.getRecipientType());
      }
    }
  }

  protected Set<Contact> getContacts(
      EmailTemplateEmailRecipientProviderConfiguration
          emailTemplateEmailRecipientProviderConfiguration) {
    final Set<Contact> contacts = new HashSet<>();

    return contacts;
  }

  @Override
  public EmailTemplateSendRequest send(EmailTemplate emailTemplate, AbstractEntity referenceData) {
    render(emailTemplate, referenceData);

    return null;
  }

  @Override
  public Set<EmailTemplateSendRequest> send(
      EmailTemplate emailTemplate, Set<AbstractEntity> referenceDatas) {
    final Set<EmailTemplateSendRequest> emailTemplateSendRequests = new HashSet<>();

    for (AbstractEntity referenceData : referenceDatas) {
      emailTemplateSendRequests.add(send(emailTemplate, referenceData));
    }

    return emailTemplateSendRequests;
  }
}
