package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: rather than store the retry policy, create a policy class and link to it here ...
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(doNotUseGetters = true)
// @PersistenceCapable
@Entity
public class EmailSendRequest extends AbstractEntity {
  @OneToOne @JoinColumn protected Email email;

  @EqualsAndHashCode.Exclude @Column protected LocalDateTime updatedDate;
  @EqualsAndHashCode.Exclude @Column protected LocalDateTime sentDate;

  // units are seconds, timeout is set via property
  @EqualsAndHashCode.Exclude @Column protected long timeout;

  // retryAttempts is set via property (over overridden)
  @EqualsAndHashCode.Exclude @Column protected int retryAttempts;

  // shall we wait (block) or just return this request?
  @EqualsAndHashCode.Exclude @Column protected boolean synchronous = true;

  public EmailSendRequest withEmail(Email email) {
    this.email = email;
    return this;
  }

  public EmailSendRequest withTimeout(long timeout) {
    this.timeout = timeout;
    return this;
  }
}
