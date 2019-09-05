package com.walterjwhite.email.organization.api.configuration.rule;

import com.walterjwhite.email.api.model.Email;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public class AttributeRule extends AbstractRule {
  protected EmailMessageField emailMessageField;

  protected MatchType matchType;
  @EqualsAndHashCode.Exclude protected Set<String> values = new HashSet<>();

  @Override
  public boolean matches(Email email) {
    return values.stream()
            .filter(value -> matchType.matches(value, emailMessageField.get(email)) != invert)
            .count()
        > 0;
  }
}
