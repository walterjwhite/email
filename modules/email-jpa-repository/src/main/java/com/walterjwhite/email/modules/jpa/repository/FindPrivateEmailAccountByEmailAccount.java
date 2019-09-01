package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.datastore.query.AbstractQuery;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public class FindPrivateEmailAccountByEmailAddress
    extends AbstractQuery<PrivateEmailAccount, PrivateEmailAccount> {
  protected final EmailAccount emailAccount;

  public FindPrivateEmailAccountByEmailAddress(final EmailAccount emailAccount) {
    super(0, 1, PrivateEmailAccount.class, PrivateEmailAccount.class, null, true);
    this.emailAccount = emailAccount;
  }

  //    Predicate contactCondition =
  //            criteriaBuilder.equal(
  //
  // emailAccountCriteriaQueryConfiguration.getRoot().get(PrivateEmailAccount_.emailAccount),
  //                    emailAccount);
}
