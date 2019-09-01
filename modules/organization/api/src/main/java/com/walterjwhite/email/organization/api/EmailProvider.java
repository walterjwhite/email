package com.walterjwhite.email.organization.api;

import com.walterjwhite.email.api.model.Email;
import java.util.Spliterator;

// TODO: this should be a supplier?
@Deprecated
public interface EmailProvider extends Spliterator<Email> {
  void reset();
}
