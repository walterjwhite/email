package com.walterjwhite.email.organization.api;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailAddressType {
  From(String.class),
  To(String[].class),
  Cc(String[].class),
  Bcc(String[].class);

  private final Class argumentTypeClass;
}
