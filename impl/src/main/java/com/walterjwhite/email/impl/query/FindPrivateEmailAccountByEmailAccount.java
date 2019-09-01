package com.walterjwhite.email.impl.query;

import com.walterjwhite.datastore.query.AbstractSingularQuery;
import com.walterjwhite.email.api.model.EmailAccount;
import com.walterjwhite.email.api.model.PrivateEmailAccount;
import lombok.Getter;

@Getter
public class FindPrivateEmailAccountByEmailAccount
    extends AbstractSingularQuery<PrivateEmailAccount> {
  protected final EmailAccount emailAccount;

  public FindPrivateEmailAccountByEmailAccount(EmailAccount emailAccount) {
    super(PrivateEmailAccount.class, false);
    this.emailAccount = emailAccount;
  }
}
