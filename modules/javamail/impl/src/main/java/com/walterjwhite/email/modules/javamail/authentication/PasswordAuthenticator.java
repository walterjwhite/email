package com.walterjwhite.email.modules.javamail.authentication;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PasswordAuthenticator extends Authenticator {
  protected final PasswordAuthentication passwordAuthentication;

  public PasswordAuthenticator(PasswordAuthentication passwordAuthentication) {
    super();
    this.passwordAuthentication = passwordAuthentication;
  }

  protected PasswordAuthentication getPasswordAuthentication() {
    return passwordAuthentication;
  }
}
