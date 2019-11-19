package com.walterjwhite.email.cli.property;

import com.walterjwhite.property.api.annotation.DefaultValue;
import com.walterjwhite.property.api.property.ConfigurableProperty;

public interface EmailFolderName extends ConfigurableProperty {
  @DefaultValue String Default = "INBOX";
}
