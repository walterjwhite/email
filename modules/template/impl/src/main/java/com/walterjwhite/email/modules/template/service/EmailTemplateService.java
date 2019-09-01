package com.walterjwhite.email.modules.template.service;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.email.modules.template.model.EmailTemplate;
import com.walterjwhite.email.modules.template.model.EmailTemplateSendRequest;
import java.util.Set;

public interface EmailTemplateService {
  Set<EmailTemplateSendRequest> render(
      EmailTemplate emailTemplate, AbstractEntity... referenceData);
}
