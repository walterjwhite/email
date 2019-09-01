package com.walterjwhite.email.modules.javamail;

import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.api.repository.PrivateEmailAccountEntityRepository;
import com.walterjwhite.email.api.service.EmailReadService;
import com.walterjwhite.google.guice.GuiceHelper;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

public class JavaEmailReadServiceTest {

  private static final Reflections REFLECTIONS =
      new Reflections(
          "com.walterjwhite",
          TypeAnnotationsScanner.class,
          SubTypesScanner.class,
          FieldAnnotationsScanner.class);

  @Before
  public void onBefore() {
    GuiceHelper.addModules(new JavaMailTestModule(getClass()));

    //    install(new JpaPersistModule("defaultJPAUnit"));
    GuiceHelper.setup();

    //    createAccount();
  }
  //
  //  protected void createAccount() {
  //    EmailAccountCreationService emailAccountCreationService =
  //
  // GuiceHelper.getGuiceApplicationInjector().getInstance(EmailAccountCreationService.class);
  //    LOGGER.info("created account:" + emailAccountCreationService.createAccount().getId());
  //  }

  /**
   * This works, be careful when executing this on public networks.
   *
   * @throws Exception
   */
  @Test /*(timeout = 10000)*/
  public void testJavaGmail() throws Exception {
    EmailReadService emailReadService =
        GuiceHelper.getGuiceApplicationInjector().getInstance(EmailReadService.class);

    Provider<PrivateEmailAccountEntityRepository> privateEmailAccountRepositoryProvider =
        GuiceHelper.getGuiceApplicationInjector()
            .getProvider(PrivateEmailAccountEntityRepository.class);
    PrivateEmailAccount from =
        privateEmailAccountRepositoryProvider.get().findRandom(PrivateEmailAccount.class);

    emailReadService.read(from);
  }
}
