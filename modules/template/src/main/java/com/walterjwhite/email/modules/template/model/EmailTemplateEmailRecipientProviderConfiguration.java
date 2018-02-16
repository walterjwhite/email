package com.walterjwhite.email.modules.template.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import javax.mail.internet.MimeMessage;
import javax.persistence.*;

@Entity
public class EmailTemplateEmailRecipientProviderConfiguration extends AbstractEntity {
  @ManyToOne @JoinColumn protected EmailTemplate emailTemplate;

  @ManyToOne @JoinColumn
  protected EmailRecipientProviderConfiguration emailRecipientProviderConfiguration;

  @Enumerated(EnumType.STRING)
  @Column
  protected MimeMessage.RecipientType recipientType;

  public EmailTemplate getEmailTemplate() {
    return emailTemplate;
  }

  public void setEmailTemplate(EmailTemplate emailTemplate) {
    this.emailTemplate = emailTemplate;
  }

  public EmailRecipientProviderConfiguration getEmailRecipientProviderConfiguration() {
    return emailRecipientProviderConfiguration;
  }

  public void setEmailRecipientProviderConfiguration(
      EmailRecipientProviderConfiguration emailRecipientProviderConfiguration) {
    this.emailRecipientProviderConfiguration = emailRecipientProviderConfiguration;
  }

  public MimeMessage.RecipientType getRecipientType() {
    return recipientType;
  }

  public void setRecipientType(MimeMessage.RecipientType recipientType) {
    this.recipientType = recipientType;
  }
}
