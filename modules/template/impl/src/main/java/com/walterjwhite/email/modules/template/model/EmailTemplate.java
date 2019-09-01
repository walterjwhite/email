package com.walterjwhite.email.modules.template.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import com.walterjwhite.template.api.model.Template;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(doNotUseGetters = true, callSuper = true)
// @PersistenceCapable
@Entity
public class EmailTemplate extends AbstractNamedEntity {
  protected Template subjectTemplate;
  protected Template bodyTemplate;

  /** Static recipients. */
  // TODO: support a dynamic template for producing contacts
  protected Set<EmailTemplateContact> emailTemplateContacts = new HashSet<>();

  protected Set<EmailTemplateEmailRecipientProviderConfiguration>
      emailTemplateEmailRecipientProviderConfigurations = new HashSet<>();
  protected Set<EmailTemplateSendRequest> emailTemplateSendRequests;
}
