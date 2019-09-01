package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractUUIDEntity;
import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.email.api.enumeration.EmailRecipientType;
import com.walterjwhite.file.api.model.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(doNotUseGetters = true, callSuper = true)
@NoArgsConstructor
// @PersistenceCapable
@Entity
public class Email extends AbstractUUIDEntity implements Comparable<Email> {
  @ToString.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn
  protected PrivateEmailAccount from;

  @ToString.Exclude
  @Column(nullable = false, updatable = false)
  protected LocalDateTime createdDate;

  @ToString.Exclude
  @Column(nullable = false, updatable = false)
  protected LocalDateTime sentDateTime;

  @Column protected String serverId;
  // aka uuid
  @Column protected String messageId;

  @ToString.Exclude @ManyToOne @JoinColumn protected EmailConversation emailConversation;

  @ToString.Exclude
  @OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
  protected Set<EmailEmailAccount> emailEmailAccounts = new HashSet<>();

  @Column protected String subject;
  @ToString.Exclude @Lob @Column protected String body;

  @ToString.Exclude
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<File> files = new HashSet<>();

  @ToString.Exclude
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Tag> tags = new HashSet<>();

  @ToString.Exclude @ManyToOne @JoinColumn protected EmailConversation conversation;

  @ToString.Exclude
  @OneToOne(mappedBy = "email")
  protected EmailSendRequest emailSendRequest;

  public Email(
      Set<EmailAccount> toRecipients, PrivateEmailAccount from, String subject, String body) {
    this();
    if (toRecipients != null && !toRecipients.isEmpty()) {
      for (EmailAccount emailAccount : toRecipients)
        emailEmailAccounts.add(new EmailEmailAccount(emailAccount, this, EmailRecipientType.To));
    }

    this.from = from;
    this.createdDate = LocalDateTime.now();
    this.subject = subject;
    this.body = body;
  }

  @Override
  public int compareTo(Email email) {
    return createdDate.compareTo(email.getCreatedDate());
  }
}
