package com.walterjwhite.email.impl.query;

import com.walterjwhite.datastore.query.AbstractSingularQuery;
import com.walterjwhite.email.api.model.EmailAccount;
import lombok.Getter;

@Getter
public class FindEmailAccountByEmailAddress extends AbstractSingularQuery<EmailAccount> {
  protected final String emailAddress;

  public FindEmailAccountByEmailAddress(final String emailAddress) {
    super(EmailAccount.class, false);
    this.emailAddress = emailAddress;
  }
}
