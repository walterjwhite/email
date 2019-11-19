package com.walterjwhite.email.cli.provider;

import com.walterjwhite.email.cli.EmailAccountConfigurer;
import com.walterjwhite.email.cli.model.EmailOrganizationSession;
import com.walterjwhite.email.cli.property.EmailOrganizationSessionConfigurationDirectory;
import com.walterjwhite.property.impl.annotation.Property;
import javax.inject.Inject;
import javax.inject.Provider;

public class EmailOrganizationSessionProvider implements Provider<EmailOrganizationSession> {
  protected final EmailAccountConfigurer emailAccountConfigurer;
  protected final String emailOrganizationSessionConfigurationDirectory;
  protected final EmailOrganizationSession emailOrganizationSession;

  @Inject
  public EmailOrganizationSessionProvider(
      final EmailAccountConfigurer emailAccountConfigurer,
      @Property(EmailOrganizationSessionConfigurationDirectory.class)
          final String emailOrganizationSessionConfigurationDirectory) {
    this.emailAccountConfigurer = emailAccountConfigurer;
    this.emailOrganizationSessionConfigurationDirectory =
        emailOrganizationSessionConfigurationDirectory;
    emailOrganizationSession =
        emailAccountConfigurer.load(emailOrganizationSessionConfigurationDirectory);
  }

  @Override
  public EmailOrganizationSession get() {
    return emailOrganizationSession;
  }
}
