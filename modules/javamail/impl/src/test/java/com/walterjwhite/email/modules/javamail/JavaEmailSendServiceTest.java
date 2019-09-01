package com.walterjwhite.email.modules.javamail;

import com.walterjwhite.email.api.model.*;
import com.walterjwhite.google.guice.GuiceHelper;
import org.junit.Before;

public class JavaEmailSendServiceTest {

  @Before
  public void onBefore() throws Exception {
    GuiceHelper.addModules(new JavaMailTestModule(getClass()));
    GuiceHelper.setup();
  }

  /**
   * This works, be careful when executing this on public networks.
   *
   * @throws Exception
   */
  //  @Test(timeout = 10000)
  //  public void testJavaGmail() throws Exception {
  //    EmailSendService emailSendService =
  //        GuiceHelper.getGuiceApplicationInjector().getInstance(EmailSendService.class);
  //
  //    final Map<String, String> settings = new HashMap<>();
  //
  //    PrivateEmailAccount privateEmailAccount =
  //        new PrivateEmailAccount(
  //            new EmailAccount(emailAddress), emailProvider, emailAddress, password);
  //    Set<EmailAccount> toRecipients = new HashSet<>();
  //    toRecipients.add(privateEmailAccount.getEmailAccount());
  //
  //    Email email =
  //        new Email(
  //            toRecipients,
  //            privateEmailAccount,
  //            "testing - javamail - email service",
  //            "This is only a test.\n\n");
  //
  //    emailSendService.send(email);
  //  }
}
