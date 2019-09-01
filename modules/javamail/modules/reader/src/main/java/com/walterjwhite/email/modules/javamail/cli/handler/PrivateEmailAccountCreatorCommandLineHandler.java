package com.walterjwhite.email.modules.javamail.cli.handler;

import com.google.inject.persist.Transactional;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.google.guice.cli.property.CommandLineHandlerShutdownTimeout;
import com.walterjwhite.google.guice.cli.service.AbstractCommandLineHandler;
import com.walterjwhite.property.api.SecretService;
import com.walterjwhite.property.impl.DefaultSecretService;
import com.walterjwhite.property.impl.annotation.Property;
import com.walterjwhite.serialization.api.service.SerializationService;
import java.io.*;
import java.nio.charset.Charset;
import javax.inject.Inject;
import javax.inject.Provider;
import org.apache.commons.io.IOUtils;

public class PrivateEmailAccountCreatorCommandLineHandler extends AbstractCommandLineHandler {

  protected final Provider<PrivateEmailAccountRepository> privateEmailAccountRepositoryProvider;
  protected final SerializationService serializationService;
  protected final SecretService secretService;

  @Inject
  public PrivateEmailAccountCreatorCommandLineHandler(
      @Property(CommandLineHandlerShutdownTimeout.class) int shutdownTimeoutInSeconds,
      Provider<PrivateEmailAccountRepository> privateEmailAccountRepositoryProvider,
      SerializationService serializationService
      /*SecretService secretService*/ ) {
    super(shutdownTimeoutInSeconds);
    this.privateEmailAccountRepositoryProvider = privateEmailAccountRepositoryProvider;
    this.serializationService = serializationService;
    //    this.secretService = secretService;
    this.secretService = new DefaultSecretService();
  }

  @Override
  public void run(String... arguments) throws Exception {
    for (final String argument : arguments) {
      try {
        createAccount(argument);
      } catch (Exception e) {
        LOGGER.error("Error creating account", e);
      }
    }
  }

  // only works with google guice annotations
  @Transactional
  protected PrivateEmailAccount createAccount(final String argument) throws IOException {
    final String decryptedContents =
        secretService.decrypt(
            IOUtils.toString(
                new BufferedInputStream(new FileInputStream(new File(argument))),
                Charset.defaultCharset()));

    PrivateEmailAccount privateEmailAccount =
        (PrivateEmailAccount)
            serializationService.deserialize(
                new ByteArrayInputStream(decryptedContents.getBytes(Charset.defaultCharset())),
                PrivateEmailAccount.class);
    privateEmailAccountRepositoryProvider.get().persist(privateEmailAccount);

    return privateEmailAccount;
  }

  @Override
  protected void onShutdown() throws Exception {}
}
