package com.walterjwhite.email.modules.javamail.cli;

import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.data.pipe.modules.email.javamail.JavaMailEmailPipeModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.api.repository.CriteriaBuilderModule;
import com.walterjwhite.email.modules.javamail.JavaMailEmailModule;
import com.walterjwhite.email.modules.javamail.cli.handler.JavaMailEmailReaderCommandLineHandler;
import com.walterjwhite.email.modules.javamail.cli.handler.PrivateEmailAccountCreatorCommandLineHandler;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.file.providers.local.service.FileStorageModule;
import com.walterjwhite.google.guice.cli.AbstractCommandLineModule;
import com.walterjwhite.property.api.PropertyManager;
import com.walterjwhite.serialization.modules.jackson.JacksonSerializationServiceModule;
import org.reflections.Reflections;

public class JavaMailEmailReaderModule extends AbstractCommandLineModule {
  public JavaMailEmailReaderModule(PropertyManager propertyManager, Reflections reflections) {
    super(propertyManager, reflections, JavaMailEmailReaderOperatingMode.class);
  }

  @Override
  protected void doCliConfigure() {
    bind(PrivateEmailAccountCreatorCommandLineHandler.class);
    bind(JavaMailEmailReaderCommandLineHandler.class);

    install(new JavaMailEmailModule());
    install(new JavaMailEmailPipeModule());

    install(new FileStorageModule());
    install(new EncryptionModule());
    install(new CompressionModule());

    install(new CriteriaBuilderModule());
    install(new GoogleGuicePersistModule(/*propertyManager, reflections*/ ));

    install(new EncryptionModule());
    install(new JacksonSerializationServiceModule());
  }
}
