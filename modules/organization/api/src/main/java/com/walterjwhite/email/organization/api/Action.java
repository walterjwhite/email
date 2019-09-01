package com.walterjwhite.email.organization.api;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;

public interface Action {
  void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      final EmailMatcherRule emailMatcherRule,
      final Email email);
}
