package com.walterjwhite.email.modules.template;

import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.api.repository.CriteriaBuilderModule;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.google.guice.property.test.GuiceTestModule;
import com.walterjwhite.google.guice.property.test.PropertyValuePair;
import com.walterjwhite.template.providers.stringtemplate.StringTemplateModule;

public class EmailTemplateTestModule extends GuiceTestModule {

  public EmailTemplateTestModule(Class testClass, PropertyValuePair... propertyValuePairs) {
    super(testClass, propertyValuePairs);
  }

  @Override
  protected void configure() {
    super.configure();

    install(new EncryptionModule());
    install(new GoogleGuicePersistModule(/*propertyManager, reflections*/ ));
    install(new CriteriaBuilderModule());

    install(new EmailTemplateModule());

    install(new StringTemplateModule());
  }
}
