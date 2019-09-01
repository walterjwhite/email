package com.walterjwhite.email.organization;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.Action;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.infrastructure.inject.core.helper.ApplicationHelper;
import com.walterjwhite.logging.annotation.LogStackTrace;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailOrganizer {
  protected final String folderName;
  protected final PrivateEmailAccount privateEmailAccount;
  protected final Supplier<Email> newEmailSupplier;
  protected final List<EmailMatcherRule> emailMatcherRules;

  public void process() {
    Stream.generate(newEmailSupplier).forEach(emailMessage -> run(emailMessage));
  }

  protected void run(Email email) {
    for (EmailMatcherRule emailMatcherRule : emailMatcherRules) {
      if (emailMatcherRule.matches(email)) {
        executeActions(emailMatcherRule, email);
        break;
      }
    }
  }

  protected void executeActions(EmailMatcherRule emailMatcherRule, Email email) {
    emailMatcherRule.initializeActionClasses();

    emailMatcherRule.getActionClasses().stream()
        .forEach(actionClass -> executeAction(emailMatcherRule, email, actionClass));
  }

  protected void executeAction(
      EmailMatcherRule emailMatcherRule, Email email, final Class<? extends Action> actionClass) {
    try {
      final Action action = getActionInstance(actionClass);
      action.execute(folderName, privateEmailAccount, emailMatcherRule, email);
    } catch (Exception e) {
      onExecutionError(e);
    }
  }

  @LogStackTrace
  protected void onExecutionError(Exception e) {}

  protected Action getActionInstance(final Class<? extends Action> actionClass) {
    // TODO: rather than rely on injection, we can create these programmatically as well
    return ApplicationHelper.getApplicationInstance().getInjector().getInstance(actionClass);
  }
}
