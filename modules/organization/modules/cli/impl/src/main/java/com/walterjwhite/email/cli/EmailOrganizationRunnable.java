package com.walterjwhite.email.cli;

import com.walterjwhite.email.organization.EmailOrganizer;
import com.walterjwhite.logging.annotation.LogStackTrace;
import com.walterjwhite.logging.enumeration.LogLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailOrganizationRunnable implements Runnable {
  protected final EmailOrganizer emailOrganizer;

  @Override
  public void run() {
    try {
      emailOrganizer.process();
    } catch (IndexOutOfBoundsException e) {
      onIndexOutOfBoundsException(e);
    }
  }

  @LogStackTrace(level = LogLevel.WARN)
  protected void onIndexOutOfBoundsException(IndexOutOfBoundsException e) {}
}
