package com.walterjwhite.email.organization.plugins.craigslist;

import com.walterjwhite.property.api.annotation.DefaultValue;
import com.walterjwhite.property.api.property.ConfigurableProperty;

public interface CraigslistLinkWritePath extends ConfigurableProperty {
  @DefaultValue String Default = "/tmp/craigslist-links";
}
