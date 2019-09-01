package com.walterjwhite.email.impl.property;

import com.walterjwhite.property.api.annotation.DefaultValue;
import com.walterjwhite.property.api.property.ConfigurableProperty;

public interface EmailSendRetryAttempts extends ConfigurableProperty {
  @DefaultValue long DEFAULT = 3;
}
