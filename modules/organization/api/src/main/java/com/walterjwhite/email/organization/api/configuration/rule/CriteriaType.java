package com.walterjwhite.email.organization.api.configuration.rule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CriteriaType {
  Must(false, true),
  Should(true, false);

  private final boolean canExitAfterFirstMatch;
  private final boolean exitAfterFirstNonMatch;
}
