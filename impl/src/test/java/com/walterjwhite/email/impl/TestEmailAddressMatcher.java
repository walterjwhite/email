package com.walterjwhite.email.impl;

import org.junit.Test;

public class TestEmailAddressMatcher {

  @Test
  public void testMatcher() {
    final String[] emails =
        new String[] {
          "First.Last@company.com", "First Last <First.Last@company.com>", "First Last"
        };

    for (final String email : emails) {
      try {
        final String[] emailParts = EmailAddressUtil.getEmailAddressParts(email);
        LOGGER.debug("name:" + emailParts[0]);
        LOGGER.debug("email:" + emailParts[1]);
      } catch (IllegalArgumentException e) {
        LOGGER.debug("invalid email address", e);
      }
    }
  }
}
