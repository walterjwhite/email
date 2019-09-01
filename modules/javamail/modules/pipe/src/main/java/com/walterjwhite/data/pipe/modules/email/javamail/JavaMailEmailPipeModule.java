package com.walterjwhite.data.pipe.modules.email.javamail;

import com.google.inject.AbstractModule;
import com.walterjwhite.data.pipe.modules.email.javamail.sink.EmailJPASink;

// import com.walterjwhite.data.pipe.modules.cli.sink.JavaMailSink;

public class JavaMailEmailPipeModule extends AbstractModule {
  @Override
  protected void configure() {
    //    bind(JavaMailSink.class);
    bind(EmailJPASink.class);
    bind(JavaMailEmailReaderPipe.class);
  }
}
