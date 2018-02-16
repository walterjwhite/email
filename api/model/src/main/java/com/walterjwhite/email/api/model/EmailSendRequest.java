package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class EmailSendRequest extends AbstractEntity {
  @OneToOne @JoinColumn protected Email email;

  @Column protected LocalDateTime updatedDate;
  @Column protected LocalDateTime sentDate;

  // units are seconds, timeout is set via property
  @Column protected long timeout;

  // retryAttempts is set via property (over overridden)
  @Column protected int retryAttempts;

  // shall we wait (block) or just return this request?
  @Column protected boolean synchronous = true;

  public Email getEmail() {
    return email;
  }

  public void setEmail(Email email) {
    this.email = email;
  }

  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(LocalDateTime updatedDate) {
    this.updatedDate = updatedDate;
  }

  public LocalDateTime getSentDate() {
    return sentDate;
  }

  public void setSentDate(LocalDateTime sentDate) {
    this.sentDate = sentDate;
  }

  public long getTimeout() {
    return timeout;
  }

  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public boolean isSynchronous() {
    return synchronous;
  }

  public void setSynchronous(boolean synchronous) {
    this.synchronous = synchronous;
  }

  public int getRetryAttempts() {
    return retryAttempts;
  }

  public void setRetryAttempts(int retryAttempts) {
    this.retryAttempts = retryAttempts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    EmailSendRequest that = (EmailSendRequest) o;
    return Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {

    return Objects.hash(super.hashCode(), email);
  }
}
