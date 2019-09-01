package com.walterjwhite.email.modules.template.service;

import com.walterjwhite.email.api.model.EmailAccount;
import java.util.Set;

public interface EmailRecipientProvider {
  Set<EmailAccount> getRecipients(Object... referenceData);
}
