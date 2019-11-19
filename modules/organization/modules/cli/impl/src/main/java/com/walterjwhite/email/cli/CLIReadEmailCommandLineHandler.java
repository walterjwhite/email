package com.walterjwhite.email.cli;

import com.walterjwhite.email.cli.model.EmailAccountRules;
import com.walterjwhite.email.cli.model.EmailOrganizationSession;
import com.walterjwhite.inject.cli.property.CommandLineHandlerShutdownTimeout;
import com.walterjwhite.inject.cli.service.AbstractCommandLineHandler;
import com.walterjwhite.property.impl.annotation.Property;
import com.walterjwhite.queue.api.service.WaitTillAllComplete;
import java.util.concurrent.Executors;
import javax.inject.Inject;

public class CLIReadEmailCommandLineHandler extends AbstractCommandLineHandler {
  protected final WaitTillAllComplete waitTillAllComplete =
      new WaitTillAllComplete(
          Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));
  protected final EmailOrganizationSession emailOrganizationSession;

  @Inject
  public CLIReadEmailCommandLineHandler(
      @Property(CommandLineHandlerShutdownTimeout.class) int shutdownTimeoutInSeconds,
      EmailOrganizationSession emailOrganizationSession) {
    super(shutdownTimeoutInSeconds);
    this.emailOrganizationSession = emailOrganizationSession;
  }

  @Override
  protected void doRun(String... arguments) {
    emailOrganizationSession.getEmailAccountRules().stream()
        .forEach(emailAccountRules -> organizeEmails(emailAccountRules));

    waitTillAllComplete.waitForAll();
  }

  protected void organizeEmails(EmailAccountRules emailAccountRules) {
    waitTillAllComplete.submit(new EmailAccountRulesRunnable(emailAccountRules));
  }
}
