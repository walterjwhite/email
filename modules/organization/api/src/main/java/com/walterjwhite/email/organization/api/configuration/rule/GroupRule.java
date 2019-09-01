package com.walterjwhite.email.organization.api.configuration.rule;

import com.walterjwhite.email.api.model.Email;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class GroupRule extends AbstractRule {
  protected Set<AbstractRule> rules = new HashSet<>();
  @EqualsAndHashCode.Exclude protected CriteriaType criteriaType;

  @Override
  public boolean matches(Email email) {
    int matchCount = 0;
    for (final AbstractRule rule : rules) {
      if (rule.matches(email) != invert) {
        matchCount++;
        if (criteriaType.isCanExitAfterFirstMatch()) break;
      } else {
        if (criteriaType.isExitAfterFirstNonMatch()) {
          return false;
        }
      }
    }

    return matchCount > 0;
  }
}
