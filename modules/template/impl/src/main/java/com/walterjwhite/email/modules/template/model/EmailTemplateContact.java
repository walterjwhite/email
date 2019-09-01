package com.walterjwhite.email.modules.template.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import com.walterjwhite.email.api.model.EmailAccount;
import javax.persistence.Entity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable
@Entity
public class EmailTemplateContact extends AbstractEntity {
  protected EmailAccount emailAccount;

  protected EmailTemplate emailTemplate;

  protected EmailRecipientType emailRecipientType;

  public EmailTemplateContact(
      EmailAccount emailAccount,
      EmailTemplate emailTemplate,
      EmailRecipientType emailRecipientType) {
    super();
    this.emailAccount = emailAccount;
    this.emailTemplate = emailTemplate;
    this.emailRecipientType = emailRecipientType;
  }

  public EmailTemplateContact() {
    super();
  }
}
