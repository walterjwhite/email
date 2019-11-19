package com.walterjwhite.email.cli.model;

import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.cli.EmailOrganizationRunnable;
import com.walterjwhite.email.cli.property.CleanupType;
import com.walterjwhite.email.organization.EmailOrganizer;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.logging.annotation.LogStackTrace;
import com.walterjwhite.logging.enumeration.LogLevel;
import com.walterjwhite.queue.api.service.WaitTillAllComplete;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Executors;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public class EmailAccountRules implements Serializable {
  // protected String folderName;
  protected CleanupType cleanupType;
  protected PrivateEmailAccount emailAccount;
  protected String folderName;
  @ToString.Exclude protected List<EmailMatcherRule> emailMatcherRules;

  //  protected transient EmailOrganizer emailOrganizer;

  public void process() {
    final EmailOrganizer[] emailOrganizers =
        cleanupType.build(emailAccount, folderName, emailMatcherRules);

    final WaitTillAllComplete waitTillAllComplete =
        new WaitTillAllComplete(Executors.newFixedThreadPool(emailOrganizers.length));

    for (final EmailOrganizer emailOrganizer : emailOrganizers) {
      try {
        waitTillAllComplete.submit(new EmailOrganizationRunnable(emailOrganizer));
      } catch (Exception e) {
        onException(e);
      }
    }

    waitTillAllComplete.waitForAll();
  }

  @LogStackTrace(level = LogLevel.WARN)
  protected void onException(final Exception e) {}
}
