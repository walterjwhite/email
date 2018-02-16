package com.walterjwhite.email.modules.javamail;

import com.google.inject.persist.jpa.JpaPersistModule;
import com.walterjwhite.compression.modules.CompressionModule;
import com.walterjwhite.datastore.GoogleGuicePersistModule;
import com.walterjwhite.datastore.criteria.CriteriaBuilderModule;
import com.walterjwhite.email.api.enumeration.EmailProviderType;
import com.walterjwhite.email.api.model.*;
import com.walterjwhite.email.api.service.EmailSendService;
import com.walterjwhite.encryption.impl.EncryptionModule;
import com.walterjwhite.encryption.property.IVFilePath;
import com.walterjwhite.encryption.property.KeyFilePath;
import com.walterjwhite.file.providers.local.service.FileStorageModule;
import com.walterjwhite.google.guice.GuiceHelper;
import com.walterjwhite.google.guice.property.test.GuiceTestModule;
import com.walterjwhite.google.guice.property.test.PropertyValuePair;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaEmailSendServiceTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaEmailSendServiceTest.class);

  @Before
  public void onBefore() throws Exception {
    GuiceHelper.addModules(
        new JavaMailEmailModule(),
        new FileStorageModule(),
        new EncryptionModule(),
        new CompressionModule(),
        new CriteriaBuilderModule(),
        new GoogleGuicePersistModule(),
        new JpaPersistModule("defaultJPAUnit"),
        new GuiceTestModule(
            new Reflections(
                "com.walterjwhite",
                TypeAnnotationsScanner.class,
                SubTypesScanner.class,
                FieldAnnotationsScanner.class),
            new PropertyValuePair(IVFilePath.class, "/etc/remote/iv"),
            new PropertyValuePair(KeyFilePath.class, "/etc/remote/key")));
    //    install(new JpaPersistModule(propertyManager.getValue(JPAUnit.class)));
    GuiceHelper.setup();
  }

  /**
   * This works, be careful when executing this on public networks.
   *
   * @throws Exception
   */
  @Test(timeout = 10000)
  public void testJavaGmail() throws Exception {
    EmailSendService emailSendService =
        GuiceHelper.getGuiceInjector().getInstance(EmailSendService.class);

    final Map<String, String> settings = new HashMap<>();
    settings.put("mail.smtp.host", "smtp.gmail.com");
    settings.put("mail.smtp.socketFactory.port", "465");
    settings.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    settings.put("mail.smtp.auth", "true");
    settings.put("mail.smtp.port", "465");

    EmailProvider emailProvider =
        new EmailProvider("gmail", "gmail.com", EmailProviderType.Default, settings);
    EmailAccount emailAccount =
        new EmailAccount(
            JavaEmailAccount.EMAIL_ADDRESS,
            emailProvider,
            JavaEmailAccount.EMAIL_ADDRESS,
            JavaEmailAccount.PASSWORD);
    Set<Contact> toRecipients = new HashSet<>();
    toRecipients.add(emailAccount);

    Email email =
        new Email(
            toRecipients,
            emailAccount,
            "testing - javamail - email service",
            "This is only a test.\n\n");

    emailSendService.send(email);
  }
}
