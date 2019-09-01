package com.walterjwhite.email.organization.plugins.count;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.Action;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.NoArgsConstructor;

@NoArgsConstructor(onConstructor_ = @Inject)
public class CountAction implements Action {
  protected final Map<Object, Integer> countMap = new HashMap<>();

  @Override
  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      EmailMatcherRule emailMatcherRule,
      final Email email) {
    final Object key = getKey();
    update(key, getCount(key));
  }

  // TODO: make this configurable
  protected Object getKey() {
    return LocalDate.now();
  }

  protected int getCount(final Object key) {
    if (countMap.containsKey(key)) return countMap.get(key);

    return 0;
  }

  protected void update(final Object key, final int count) {
    countMap.put(key, count + 1);
  }
}
