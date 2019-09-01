package com.walterjwhite.email.impl;

import com.walterjwhite.email.api.enumeration.EmailProviderType;
import com.walterjwhite.email.api.model.PrivateEmailAccount;

public class EmailProviderSelector {
  public EmailProviderType get(PrivateEmailAccount privateEmailAccount) {
    return privateEmailAccount.getProvider().getType();
  }
}
