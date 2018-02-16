package com.walterjwhite.email.modules.template.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import com.walterjwhite.email.api.model.Contact;
import com.walterjwhite.template.api.model.Template;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class EmailTemplate extends AbstractNamedEntity {
  @ManyToOne @JoinColumn protected Template subjectTemplate;
  @ManyToOne @JoinColumn protected Template bodyTemplate;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Contact> toRecipients = new HashSet<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Contact> ccRecipients = new HashSet<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Contact> bccRecipients = new HashSet<>();

  @OneToMany(mappedBy = "emailTemplate")
  protected Set<EmailTemplateEmailRecipientProviderConfiguration>
      emailTemplateEmailRecipientProviderConfigurations = new HashSet<>();

  @OneToMany(mappedBy = "emailTemplate")
  protected Set<EmailTemplateSendRequest> emailTemplateSendRequests;

  public Template getSubjectTemplate() {
    return subjectTemplate;
  }

  public void setSubjectTemplate(Template subjectTemplate) {
    this.subjectTemplate = subjectTemplate;
  }

  public Template getBodyTemplate() {
    return bodyTemplate;
  }

  public void setBodyTemplate(Template bodyTemplate) {
    this.bodyTemplate = bodyTemplate;
  }

  public Set<Contact> getToRecipients() {
    return toRecipients;
  }

  public void setToRecipients(Set<Contact> toRecipients) {
    this.toRecipients = toRecipients;
  }

  public Set<Contact> getCcRecipients() {
    return ccRecipients;
  }

  public void setCcRecipients(Set<Contact> ccRecipients) {
    this.ccRecipients = ccRecipients;
  }

  public Set<Contact> getBccRecipients() {
    return bccRecipients;
  }

  public void setBccRecipients(Set<Contact> bccRecipients) {
    this.bccRecipients = bccRecipients;
  }

  public Set<EmailTemplateSendRequest> getEmailTemplateSendRequests() {
    return emailTemplateSendRequests;
  }

  public void setEmailTemplateSendRequests(
      Set<EmailTemplateSendRequest> emailTemplateSendRequests) {
    this.emailTemplateSendRequests = emailTemplateSendRequests;
  }

  public Set<EmailTemplateEmailRecipientProviderConfiguration>
      getEmailTemplateEmailRecipientProviderConfigurations() {
    return emailTemplateEmailRecipientProviderConfigurations;
  }

  public void setEmailTemplateEmailRecipientProviderConfigurations(
      Set<EmailTemplateEmailRecipientProviderConfiguration>
          emailTemplateEmailRecipientProviderConfigurations) {
    this.emailTemplateEmailRecipientProviderConfigurations =
        emailTemplateEmailRecipientProviderConfigurations;
  }
}
