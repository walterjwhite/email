package com.walterjwhite.email.impl;

import com.walterjwhite.google.guice.property.property.DefaultValue;
import com.walterjwhite.google.guice.property.property.GuiceProperty;

public interface EmailFolder extends GuiceProperty {
  @DefaultValue String Default = "Inbox";
}
