package com.walterjwhite.email.impl;

import com.walterjwhite.google.guice.property.property.DefaultValue;
import com.walterjwhite.google.guice.property.property.GuiceProperty;

public interface EmailSendTimeout extends GuiceProperty {
  @DefaultValue long DEFAULT = 30;
}
