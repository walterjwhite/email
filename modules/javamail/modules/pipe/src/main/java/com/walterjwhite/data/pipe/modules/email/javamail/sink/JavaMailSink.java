// package com.walterjwhite.data.pipe.modules.cli.sink;
//
// import com.walterjwhite.data.pipe.impl.AbstractSink;
// import com.walterjwhite.data.pipe.modules.cli.model.JavaMailSinkConfiguration;
// import com.walterjwhite.email.api.model.Email;
// import com.walterjwhite.email.modules.javamail.service.JavaMailEmailSendService;
// import javax.inject.Inject;
//
// public class JavaMailSink extends AbstractSink<Email, JavaMailSinkConfiguration> {
//  protected final JavaMailEmailSendService javaMailEmailSendService;
//
//  @Inject
//  public JavaMailSink(JavaMailEmailSendService javaMailEmailSendService) {
//    this.javaMailEmailSendService = javaMailEmailSendService;
//  }
//
//  @Override
//  protected void doConfigure() {}
//
//  @Override
//  public void close() throws Exception {}
//
//  @Override
//  public void accept(Email email) {
//    try {
//      javaMailEmailSendService.send(email);
//    } catch (Exception e) {
//      throw new RuntimeException("Error sending email", e));
//    }
//  }
// }
