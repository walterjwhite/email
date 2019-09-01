package com.walterjwhite.email.organization.plugins.reply;

import com.walterjwhite.email.organization.api.Action;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;

public interface MoveAction extends Action {
  default String getTargetFolderId(EmailMatcherRule emailMatcherRule) {
    return emailMatcherRule.getName();
  }
}
