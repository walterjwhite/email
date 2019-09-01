package com.walterjwhite.email.modules.javamail.cli.handler;

// import JavaMailEmailReaderPipe;

import com.walterjwhite.data.pipe.modules.email.javamail.JavaMailEmailReaderPipe;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.google.guice.cli.property.CommandLineHandlerShutdownTimeout;
import com.walterjwhite.google.guice.cli.service.AbstractCommandLineHandler;
import com.walterjwhite.property.impl.annotation.Property;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.mail.MessagingException;

public class JavaMailEmailReaderCommandLineHandler extends AbstractCommandLineHandler {
  protected final Provider<PrivateEmailAccountRepository> privateEmailAccountRepositoryProvider;
  protected final JavaMailEmailReaderPipe javaMailEmailReaderPipe;

  @Inject
  public JavaMailEmailReaderCommandLineHandler(
      @Property(CommandLineHandlerShutdownTimeout.class) int shutdownTimeoutInSeconds,
      Provider<PrivateEmailAccountRepository> privateEmailAccountRepositoryProvider,
      JavaMailEmailReaderPipe javaMailEmailReaderPipe) {
    super(shutdownTimeoutInSeconds);
    this.privateEmailAccountRepositoryProvider = privateEmailAccountRepositoryProvider;
    this.javaMailEmailReaderPipe = javaMailEmailReaderPipe;
  }

  @Override
  public void run(String... arguments) throws Exception {
    for (final String argument : arguments) {
      read(argument);
    }
  }

  protected PrivateEmailAccount read(final String privateEmailAccountId) throws MessagingException {
    PrivateEmailAccount privateEmailAccount = null;
    // privateEmailAccountRepositoryProvider.get().findById(privateEmailAccountId);
    if (privateEmailAccount == null) return null;

    javaMailEmailReaderPipe.run(privateEmailAccount);

    return privateEmailAccount;
  }

  @Override
  protected void onShutdown() throws Exception {}
}
