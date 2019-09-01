package com.walterjwhite.email.organization.api;

import com.walterjwhite.email.api.model.Email;

public interface EmailMatcher {
  boolean matches(Email email);
}
