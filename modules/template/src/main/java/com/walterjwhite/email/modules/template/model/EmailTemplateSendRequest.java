package com.walterjwhite.email.modules.template.model;

import com.walterjwhite.datastore.api.model.entity.EntityReference;
import com.walterjwhite.email.api.model.EmailSendRequest;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class EmailTemplateSendRequest extends EmailSendRequest {
  @ManyToOne @JoinColumn protected EmailTemplate emailTemplate;
  // TODO: support more than one reference data here

  @ManyToOne @JoinColumn protected EntityReference referenceData;

  public EmailTemplate getEmailTemplate() {
    return emailTemplate;
  }

  public void setEmailTemplate(EmailTemplate emailTemplate) {
    this.emailTemplate = emailTemplate;
  }

  public EntityReference getReferenceData() {
    return referenceData;
  }

  public void setReferenceData(EntityReference referenceData) {
    this.referenceData = referenceData;
  }
}
