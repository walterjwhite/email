package com.walterjwhite.email.api.service;

import com.walterjwhite.email.api.model.EmailAccount;
import java.io.IOException;

public interface EmailReadService {
  void read(EmailAccount emailAccount) throws IOException;
}
