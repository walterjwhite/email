package com.walterjwhite.email.api.service;

import com.walterjwhite.email.api.model.Email;
import com.walterjwhite.email.api.model.EmailSendRequest;

public interface EmailSendService {
  EmailSendRequest send(Email email) throws Exception;

  void send(EmailSendRequest emailSendRequest) throws Exception;
}
