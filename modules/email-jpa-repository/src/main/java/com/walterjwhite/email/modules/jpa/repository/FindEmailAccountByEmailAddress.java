package com.walterjwhite.file.providers.local.service;

import com.walterjwhite.datastore.query.AbstractQuery;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public class FindEmailAccountByEmailAddress extends AbstractQuery<EmailAddress, EmailAddress> {
  protected final String emailAddress;

  public FindEmailAccountByEmailAddress(final String emailAddress) {
    super(0, 1, EmailAddress.class, EmailAddress.class, null, true);
    this.emailAddress = emailAddress;
  }

  //    Predicate entityTypeNameCondition =
  //            criteriaBuilder.equal(
  //                    contactCriteriaQueryConfiguration.getRoot().get(EmailAccount_.emailAddress),
  //                    emailAddress);
}
