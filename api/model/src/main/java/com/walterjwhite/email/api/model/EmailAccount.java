package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class EmailAccount extends AbstractEntity {
  @EqualsAndHashCode.Exclude @Column protected String name;

  @Column(unique = true, nullable = false)
  protected String emailAddress;

  public EmailAccount withEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  public EmailAccount withName(final String name) {
    this.name = name;
    return this;
  }
}
