package com.walterjwhite.email.impl.property;

import com.walterjwhite.property.api.annotation.DefaultValue;
import com.walterjwhite.property.api.property.ConfigurableProperty;

public interface MaximumAttachmentSize extends ConfigurableProperty {
  @DefaultValue int Default = 5242880;
}
