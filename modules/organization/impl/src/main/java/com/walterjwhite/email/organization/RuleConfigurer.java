package com.walterjwhite.email.organization;

import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import com.walterjwhite.email.organization.api.property.EmailRuleExtension;
import com.walterjwhite.property.impl.annotation.Property;
import com.walterjwhite.serialization.api.service.SerializationService;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class RuleConfigurer {
  private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfigurer.class);
  protected final SerializationService serializationService;
  protected final String emailRuleExtension;

  @Inject
  public RuleConfigurer(
      final SerializationService serializationService,
      @Property(EmailRuleExtension.class) String emailRuleExtension) {
    this.serializationService = serializationService;
    this.emailRuleExtension = emailRuleExtension;
  }

  public List<EmailMatcherRule> load(final String rulePath) {
    final List<EmailMatcherRule> emailMatcherRules = new ArrayList<>();

    final File root = new File(rulePath);
    loadAll(emailMatcherRules, root, root);
    Collections.sort(emailMatcherRules);

    return emailMatcherRules;
  }

  protected void loadAll(
      final List<EmailMatcherRule> emailMatcherRules,
      final File rootDirectory,
      final File absoluteRootDirectory) {
    for (final File file : rootDirectory.listFiles()) {
      if (file.isDirectory()) {
        loadAll(emailMatcherRules, file, absoluteRootDirectory);
      } else {
        addRule(emailMatcherRules, read(emailRuleExtension, file, absoluteRootDirectory));
      }
    }
  }

  protected void addRule(
      List<EmailMatcherRule> emailMatcherRules, final EmailMatcherRule emailMatcherRule) {
    if (emailMatcherRule == null) return;

    emailMatcherRules.add(emailMatcherRule);
  }

  protected EmailMatcherRule read(
      final String fileExtension, final File file, final File absoluteRootDirectory) {
    try {
      final EmailMatcherRule emailMatcherRule =
          serializationService.deserialize(new FileInputStream(file), EmailMatcherRule.class);
      emailMatcherRule.setName(getRuleName(fileExtension, absoluteRootDirectory, file));
      return emailMatcherRule;
    } catch (Exception e) {
      LOGGER.error("Error reading rule: ", absoluteRootDirectory, file, e);
      return null;
    }
  }

  protected String getRuleName(
      final String fileExtension, final File rootDirectory, final File file) {
    return file.getAbsolutePath()
        .substring(rootDirectory.getAbsolutePath().length() + 1)
        .replaceAll("\\\\", "/")
        .replace(fileExtension, "");
  }
}
