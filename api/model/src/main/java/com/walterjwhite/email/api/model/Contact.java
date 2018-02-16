package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import javax.persistence.Entity;

@Entity
public class Contact extends AbstractNamedEntity {
  public Contact(String emailAddress) {
    super(emailAddress);
  }

  public Contact() {
    super();
  }

  @Override
  public String toString() {
    return "Contact{" + "emailAddress='" + name + '\'' + '}';
  }
}
