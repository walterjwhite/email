package com.walterjwhite.email.modules.javamail.cli;

import com.walterjwhite.email.modules.javamail.cli.handler.JavaMailEmailReaderCommandLineHandler;
import com.walterjwhite.email.modules.javamail.cli.handler.PrivateEmailAccountCreatorCommandLineHandler;
import com.walterjwhite.google.guice.cli.property.OperatingMode;
import com.walterjwhite.google.guice.cli.service.AbstractCommandLineHandler;
import com.walterjwhite.property.api.annotation.DefaultValue;

public enum JavaMailEmailReaderOperatingMode implements OperatingMode {
  @DefaultValue
  Default(JavaMailEmailReaderCommandLineHandler.class),
  CreateAccount(PrivateEmailAccountCreatorCommandLineHandler.class);

  private final Class<? extends AbstractCommandLineHandler> initiatorClass;

  JavaMailEmailReaderOperatingMode(Class<? extends AbstractCommandLineHandler> initiatorClass) {
    this.initiatorClass = initiatorClass;
  }

  @Override
  public Class<? extends AbstractCommandLineHandler> getInitiatorClass() {
    return initiatorClass;
  }
}
