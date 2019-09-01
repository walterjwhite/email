package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable
@Entity
public class EmailEmailAccount extends AbstractEntity {
  @ManyToOne(optional = false, cascade = CascadeType.ALL)
  @JoinColumn(nullable = false, updatable = false)
  protected EmailAccount emailAccount;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, updatable = false)
  protected Email email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  protected EmailRecipientType emailRecipientType;
}
