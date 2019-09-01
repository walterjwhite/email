package com.walterjwhite.email.modules.template;

import com.google.inject.Injector;
import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.modules.template.model.EmailTemplate;
import com.walterjwhite.email.modules.template.model.EmailTemplateContact;
import com.walterjwhite.email.modules.template.model.EmailTemplateSendRequest;
import com.walterjwhite.email.modules.template.service.EmailTemplateService;
import com.walterjwhite.google.guice.GuiceHelper;
import com.walterjwhite.template.api.model.Template;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmailTemplateTest {
  protected Injector injector;

  @Before
  public void onBefore() {
    GuiceHelper.addModules(new EmailTemplateTestModule(getClass()));

    GuiceHelper.setup();
    injector = GuiceHelper.getGuiceApplicationInjector();
  }

  @Test
  public void testTemplate() {
    EmailTemplateService emailTemplateService = injector.getInstance(EmailTemplateService.class);

    EmailTemplate emailTemplate = new EmailTemplate();
    emailTemplate.setBodyTemplate(
        new Template("EmailBodyTemplate")
            .withContents(
                "Body\nHi $SampleEntity.recipientName$,\nThis is a test.\n\nSincerely,\n$SampleEntity.senderName$"));
    emailTemplate.setSubjectTemplate(
        new Template("EmailSubjectTemplate").withContents("Test - $SampleEntity.subject$"));

    Set<EmailTemplateContact> emailTemplateContacts = new HashSet<>();
    emailTemplateContacts.add(
        new EmailTemplateContact(
            new EmailAccount().withEmailAddress("support@google.com"),
            emailTemplate,
            EmailRecipientType.To));
    emailTemplateContacts.add(
        new EmailTemplateContact(
            new EmailAccount().withEmailAddress("support@yahoo.com"),
            emailTemplate,
            EmailRecipientType.Bcc));
    emailTemplateContacts.add(
        new EmailTemplateContact(
            new EmailAccount().withEmailAddress("support@yahoo.com"),
            emailTemplate,
            EmailRecipientType.Cc));

    emailTemplate.setEmailTemplateContacts(emailTemplateContacts);

    Set<EmailTemplateSendRequest> emailTemplateSendRequests =
        emailTemplateService.render(
            emailTemplate, new SampleEntity("subject", "recipient", "sender"));
    Assert.assertEquals(1, emailTemplateSendRequests.size());

    EmailTemplateSendRequest emailTemplateSendRequest = emailTemplateSendRequests.iterator().next();
    Assert.assertEquals("Test - subject", emailTemplateSendRequest.getEmail().getSubject());
    Assert.assertEquals(
        "Body\nHi recipient,\nThis is a test.\n\nSincerely,\nsender",
        emailTemplateSendRequest.getEmail().getBody());
  }
}
