package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.encryption.enumeration.EncryptionType;
import com.walterjwhite.encryption.model.EncryptedEntity;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
@NoArgsConstructor
// @PersistenceCapable
@Entity
public class PrivateEmailAccount extends AbstractEntity {

  // TODO: change this to extends
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  protected EmailAccount emailAccount;

  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn
  protected EmailProvider provider;

  // TODO: encrypt
  @EqualsAndHashCode.Exclude @Column protected String username;

  @EqualsAndHashCode.Exclude @Embedded protected EncryptedEntity password;

  /** Only used for Exchange accounts. */
  @EqualsAndHashCode.Exclude @Column protected String domain;

  /** Holds messages, folders, etc. */
  @EqualsAndHashCode.Exclude @Column protected int folderType;

  // TODO: re-add validation here (separate entity, validate code, validation date, ...)

  public PrivateEmailAccount(
      EmailAccount emailAccount,
      EmailProvider provider,
      String username,
      String password,
      String domain) {
    this(emailAccount);
    this.provider = provider;
    this.username = username;
    this.password = new EncryptedEntity(password, EncryptionType.Encrypt); // password;
    this.domain = domain;
  }

  public PrivateEmailAccount(
      final EmailAccount emailAccount, EmailProvider provider, String username, String password) {
    this(emailAccount);
    this.provider = provider;
    this.username = username;
    this.password = new EncryptedEntity(password, EncryptionType.Encrypt); // password;
  }

  public PrivateEmailAccount(final EmailAccount emailAccount) {
    super();
    this.emailAccount = emailAccount;
  }
}
