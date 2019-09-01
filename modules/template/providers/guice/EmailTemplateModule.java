package com.walterjwhite.email.modules.template;

import com.google.inject.AbstractModule;
import com.walterjwhite.email.modules.template.service.DefaultEmailTemplateService;
import com.walterjwhite.email.modules.template.service.EmailTemplateService;

public class EmailTemplateModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EmailTemplateService.class).to(DefaultEmailTemplateService.class);
    // install(new TemplateModule());
  }
}
