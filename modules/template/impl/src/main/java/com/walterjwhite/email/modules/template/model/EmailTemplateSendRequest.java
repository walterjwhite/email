package com.walterjwhite.email.modules.template.model;

import com.walterjwhite.datastore.api.model.entity.EntityReference;
import com.walterjwhite.email.api.model.EmailSendRequest;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(doNotUseGetters = true, callSuper = true)
public class EmailTemplateSendRequest extends EmailSendRequest {
  protected EmailTemplate emailTemplate;
  // TODO: support more than one reference data here

  protected EntityReference referenceData;
}
