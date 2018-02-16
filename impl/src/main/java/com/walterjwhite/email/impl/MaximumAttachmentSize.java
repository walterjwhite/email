package com.walterjwhite.email.impl;

import com.walterjwhite.google.guice.property.property.DefaultValue;
import com.walterjwhite.google.guice.property.property.GuiceProperty;

public interface MaximumAttachmentSize extends GuiceProperty {
  @DefaultValue int Default = 5242880;
}
