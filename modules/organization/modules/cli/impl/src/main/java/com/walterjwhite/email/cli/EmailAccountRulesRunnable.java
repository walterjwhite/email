package com.walterjwhite.email.cli;

import com.walterjwhite.email.cli.model.EmailAccountRules;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailAccountRulesRunnable implements Runnable {
  protected final EmailAccountRules emailAccountRules;

  @Override
  public void run() {
    emailAccountRules.process();
  }
}
