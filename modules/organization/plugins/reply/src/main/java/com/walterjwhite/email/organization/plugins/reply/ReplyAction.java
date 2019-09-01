package com.walterjwhite.email.organization.plugins.reply;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.api.service.EmailSendService;
import com.walterjwhite.email.modules.template.model.EmailTemplate;
import com.walterjwhite.email.modules.template.model.EmailTemplateSendRequest;
import com.walterjwhite.email.modules.template.service.EmailTemplateService;
import com.walterjwhite.email.organization.api.Action;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.property.impl.annotation.Property;
import com.walterjwhite.serialization.api.service.SerializationService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import javax.inject.Inject;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ReplyAction implements Action {
  protected final EmailSendService emailSendService;
  protected final EmailTemplateService emailTemplateService;
  protected final SerializationService serializationService;

  @Property(ReplyActionEmailTemplatePath.class)
  protected final String emailTemplatePath;

  @Override
  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      EmailMatcherRule emailMatcherRule,
      Email email) {
    try {
      final EmailTemplate emailTemplate = getEmailTemplate(emailMatcherRule);

      final Set<EmailTemplateSendRequest> emailTemplateSendRequests =
          emailTemplateService.render(emailTemplate, email, emailMatcherRule);
      emailTemplateSendRequests.stream()
          .forEach(emailTemplateSendRequest -> send(emailTemplateSendRequest));
    } catch (Exception e) {
      throw new RuntimeException("Error replying to message", e);
    }
  }

  protected EmailTemplate getEmailTemplate(final EmailMatcherRule emailMatcherRule)
      throws IOException {
    return (EmailTemplate)
        serializationService.deserialize(
            new FileInputStream(getEmailTemplatePath(emailMatcherRule)), EmailTemplate.class);
  }

  protected String getEmailTemplatePath(final EmailMatcherRule emailMatcherRule) {
    return emailTemplatePath + File.separator + emailMatcherRule.getName();
  }

  protected void send(EmailTemplateSendRequest emailTemplateSendRequest) {
    try {
      emailSendService.send(emailTemplateSendRequest);
    } catch (Exception e) {
      throw new RuntimeException("Error sending email template request", e);
    }
  }
}
