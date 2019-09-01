package com.walterjwhite.email.modules.template.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import javax.persistence.Entity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable
@Entity
public class EmailTemplateEmailRecipientProviderConfiguration extends AbstractEntity {

  protected EmailRecipientProviderConfiguration emailRecipientProviderConfiguration;
  protected EmailRecipientType emailRecipientType;
}
