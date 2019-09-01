package com.walterjwhite.email.organization.providers.guice;

import com.google.inject.AbstractModule;
import com.walterjwhite.email.organization.RuleConfigurer;

public class EmailOrganizationGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    //    bind(EmailOrganizer.class);
    bind(RuleConfigurer.class);
  }
}
