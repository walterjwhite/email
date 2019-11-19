package com.walterjwhite.email.cli.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public class EmailOrganizationSession {
  protected List<EmailAccountRules> emailAccountRules = new ArrayList<>();
}
