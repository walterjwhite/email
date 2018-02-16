package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.encryption.annotation.Encrypted;
import com.walterjwhite.encryption.enumeration.EncryptionType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EmailAccount extends Contact {
  @ManyToOne @JoinColumn protected EmailProvider provider;

  // TODO: encrypt
  @Column protected String username;

  @Encrypted(encryptionType = EncryptionType.Encrypt)
  /*@Column*/ protected String password;

  @Column protected byte[] passwordEncrypted;

  @Column protected byte[] passwordSalt;

  /** Only used for Exchange accounts. */
  @Column protected String domain;

  // TODO: re-add validation here (separate entity, validate code, validation date, ...)

  public EmailAccount(
      final String emailAddress,
      EmailProvider provider,
      String username,
      String password,
      String domain) {
    super(emailAddress);
    this.provider = provider;
    this.username = username;
    this.password = password;
    this.domain = domain;
  }

  public EmailAccount(
      final String emailAddress, EmailProvider provider, String username, String password) {
    super(emailAddress);
    this.provider = provider;
    this.username = username;
    this.password = password;
  }

  public EmailAccount() {
    super();
  }

  public EmailProvider getProvider() {
    return provider;
  }

  public void setProvider(EmailProvider provider) {
    this.provider = provider;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public byte[] getPasswordEncrypted() {
    return passwordEncrypted;
  }

  public void setPasswordEncrypted(byte[] passwordEncrypted) {
    this.passwordEncrypted = passwordEncrypted;
  }

  public byte[] getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(byte[] passwordSalt) {
    this.passwordSalt = passwordSalt;
  }
}
