package com.walterjwhite.email.api.enumeration;

import com.walterjwhite.property.api.annotation.DefaultValue;
import com.walterjwhite.property.api.property.ConfigurableProperty;

public enum EmailProviderType implements ConfigurableProperty {
  @DefaultValue
  Default,
  Exchange;
}
