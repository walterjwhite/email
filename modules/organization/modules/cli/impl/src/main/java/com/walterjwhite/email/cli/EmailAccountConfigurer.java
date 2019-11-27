package com.walterjwhite.email.cli;

import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.cli.model.EmailAccountRules;
import com.walterjwhite.email.cli.model.EmailOrganizationSession;
import com.walterjwhite.email.cli.property.CleanupType;
import com.walterjwhite.email.cli.property.EmailAccountExtension;
import com.walterjwhite.email.cli.property.EmailFolderName;
import com.walterjwhite.email.organization.RuleConfigurer;
import com.walterjwhite.property.impl.annotation.Property;
import com.walterjwhite.serialization.api.service.SerializationService;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class EmailAccountConfigurer {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailAccountConfigurer.class);
  public static final String RULES_NAME = "rules";
  public static final String ACCOUNT_NAME = "account";

  protected final SerializationService serializationService;
  protected final String accountExtension;
  protected final RuleConfigurer ruleConfigurer;
  protected final CleanupType cleanupType;
  protected final String emailFolderName;

  @Inject
  public EmailAccountConfigurer(
      final SerializationService serializationService,
      @Property(EmailAccountExtension.class) String accountExtension,
      @Property(CleanupType.class) CleanupType cleanupType,
      @Property(EmailFolderName.class) String emailFolderName,
      RuleConfigurer ruleConfigurer) {
    this.serializationService = serializationService;
    this.accountExtension = accountExtension;
    this.ruleConfigurer = ruleConfigurer;
    this.cleanupType = cleanupType;
    this.emailFolderName = emailFolderName;
  }

  public EmailOrganizationSession load(final String rulePath) {
    final List<EmailAccountRules> emailAccountRules = new ArrayList<>();

    final File root = new File(rulePath);
    loadAll(emailAccountRules, root, root);

    final EmailOrganizationSession emailOrganizationSession = new EmailOrganizationSession();
    emailOrganizationSession.getEmailAccountRules().addAll(emailAccountRules);
    return emailOrganizationSession;
  }

  protected void loadAll(
      final List<EmailAccountRules> emailAccounts,
      final File rootDirectory,
      final File absoluteRootDirectory) {
    if (!rootDirectory.exists()) {
      throw new RuntimeException("Root Directory does ! exist: " + rootDirectory);
    }

    for (final File file : rootDirectory.listFiles()) {
      if (file.isDirectory()) {
        if (!RULES_NAME.equals(file.getName())) {
          loadAll(emailAccounts, file, absoluteRootDirectory);
        }
      } else {
        if ((ACCOUNT_NAME + accountExtension).equals(file.getName())) {
          addEmailAccount(emailAccounts, read(accountExtension, file, absoluteRootDirectory));
        }
      }
    }
  }

  protected void addEmailAccount(
      List<EmailAccountRules> emailAccountRulesList, final EmailAccountRules emailAccountRules) {
    if (emailAccountRules == null) return;

    emailAccountRulesList.add(emailAccountRules);
  }

  protected EmailAccountRules read(
      final String fileExtension, final File file, final File absoluteRootDirectory) {
    try {
      final PrivateEmailAccount emailAccount =
          serializationService.deserialize(new FileInputStream(file), PrivateEmailAccount.class);

      final EmailAccountRules emailAccountRules = new EmailAccountRules();
      emailAccountRules.setEmailAccount(emailAccount);
      emailAccountRules.setCleanupType(cleanupType);
      emailAccountRules.setFolderName(emailFolderName);
      emailAccountRules.setEmailMatcherRules(
          ruleConfigurer.load(file.getParent() + File.separator + RULES_NAME));

      return emailAccountRules;
    } catch (Exception e) {
      LOGGER.error("Error reading rule: ", absoluteRootDirectory, file, e);
      return null;
    }
  }
}
