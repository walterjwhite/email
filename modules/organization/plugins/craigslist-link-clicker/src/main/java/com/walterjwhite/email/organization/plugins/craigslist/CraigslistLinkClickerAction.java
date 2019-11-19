package com.walterjwhite.email.organization.plugins.craigslist;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.Action;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CraigslistLinkClickerAction implements Action {
  public static final String CRAIGSLIST_LINK_PATTERN = "https://post.craigslist.org/u/";

  @Override
  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      EmailMatcherRule emailMatcherRule,
      Email email) {
    // follow URL
    // click this button
    // *[@id="new-edit"]/div/div[2]/div[1]/button
  }

  protected String getLinkAddress(final Email email) {
    // get link in email body (after being matched by a rule)
    return Arrays.stream(email.getBody().split("\n"))
        .filter(line -> line != null && line.contains(CRAIGSLIST_LINK_PATTERN))
        .findFirst()
        .get();
  }
}
