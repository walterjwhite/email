package com.walterjwhite.email.modules.template.service;

import com.walterjwhite.email.api.model.Contact;
import java.util.Set;

public interface EmailRecipientProvider {
  Set<Contact> getRecipients();
}
