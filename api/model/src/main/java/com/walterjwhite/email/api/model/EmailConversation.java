package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable
@Entity
public class EmailConversation extends AbstractEntity {
  @Column(unique = true, nullable = false)
  protected String uuid;

  @EqualsAndHashCode.Exclude
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
}
