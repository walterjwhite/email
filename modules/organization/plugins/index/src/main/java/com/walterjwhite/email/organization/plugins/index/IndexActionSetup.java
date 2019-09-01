package com.walterjwhite.email.organization.plugins.index;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.organization.api.Action;
import com.walterjwhite.email.organization.api.configuration.rule.EmailMatcherRule;

// TODO: this should be generic so we can use ES / SOLR / etc.
public class IndexActionSetup implements Action {
  @Override
  public void execute(
      final String folderName,
      final PrivateEmailAccount privateEmailAccount,
      EmailMatcherRule emailMatcherRule,
      final Email email) {
    //    final ElasticSearchConfiguration elasticSearchConfiguration = null;
    //
    //    new CreateIndexCommand<>(elasticSearchConfiguration, Email.class).execute();
  }
}
