package com.walterjwhite.email.cli.guice;

import com.google.inject.Binder;
import com.walterjwhite.datastore.api.repository.CriteriaBuilderModule;
import com.walterjwhite.email.cli.model.EmailOrganizationSession;
import com.walterjwhite.email.cli.provider.EmailOrganizationSessionProvider;
import com.walterjwhite.email.organization.providers.guice.EmailOrganizationGuiceModule;
import com.walterjwhite.encryption.modules.guice.EncryptionModule;
import com.walterjwhite.infrastructure.inject.providers.guice.GuiceApplicationModule;
import com.walterjwhite.javamail.guice.JavaMailEmailModule;
import com.walterjwhite.modules.compression.xz.guice.XZCompressionModule;
import com.walterjwhite.modules.file.local.guice.LocalFileStorageModule;
import com.walterjwhite.serialization.modules.snakeyaml.providers.guice.SnakeyamlSerializationServiceModule;

public class CLIReadEmailModule implements GuiceApplicationModule {
  @Override
  public void configure(final Binder binder) {
    bindModules(binder);
    bindProviders(binder);
  }

  protected void bindModules(final Binder binder) {
    binder.install(new JavaMailEmailModule());
    binder.install(new LocalFileStorageModule());

    binder.install(new CriteriaBuilderModule());

    binder.install(new EncryptionModule());
    binder.install(new XZCompressionModule());

    binder.install(new EmailOrganizationGuiceModule());
    binder.install(new SnakeyamlSerializationServiceModule());
  }

  protected void bindProviders(final Binder binder) {
    binder.bind(EmailOrganizationSession.class).toProvider(EmailOrganizationSessionProvider.class);
  }
}
