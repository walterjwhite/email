package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class EmailConversation extends AbstractEntity {
  @Column(unique = true, nullable = false)
  protected String uuid;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "emailConversation")
  protected List<Email> emails;

  public EmailConversation(String uuid) {
    this();
    this.uuid = uuid;
  }

  public EmailConversation() {
    super();
    this.emails = new ArrayList<>();
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public List<Email> getEmails() {
    return emails;
  }

  public void setEmails(List<Email> emails) {
    this.emails.clear();
    this.emails.addAll(emails);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EmailConversation that = (EmailConversation) o;

    return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
  }

  @Override
  public int hashCode() {
    return uuid != null ? uuid.hashCode() : 0;
  }
}
