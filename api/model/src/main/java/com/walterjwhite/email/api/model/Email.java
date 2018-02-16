package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractEntity;
import com.walterjwhite.datastore.api.model.entity.Tag;
import com.walterjwhite.file.api.model.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Email extends AbstractEntity {
  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(nullable = false, updatable = false)
  protected EmailAccount from;

  @Column(nullable = false, updatable = false)
  protected LocalDateTime createdDate;

  @Column protected String serverId;

  @ManyToOne @JoinColumn protected EmailConversation emailConversation;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Contact> toRecipients = new HashSet<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Contact> ccRecipients = new HashSet<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Contact> bccRecipients = new HashSet<>();

  @Column protected String subject;
  @Lob @Column protected String body;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<File> files = new HashSet<>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable
  protected Set<Tag> tags;

  @ManyToOne @JoinColumn protected EmailConversation conversation;

  @OneToOne(mappedBy = "email")
  protected EmailSendRequest emailSendRequest;

  public Email(Set<Contact> toRecipients, EmailAccount from, String subject, String body) {
    this();
    if (toRecipients != null && !toRecipients.isEmpty()) this.toRecipients.addAll(toRecipients);

    this.from = from;
    this.createdDate = LocalDateTime.now();
    this.subject = subject;
    this.body = body;
  }

  public Email() {
    super();
  }

  public EmailAccount getFrom() {
    return from;
  }

  public void setFrom(EmailAccount from) {
    this.from = from;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  public EmailConversation getConversation() {
    return conversation;
  }

  public void setConversation(EmailConversation conversation) {
    this.conversation = conversation;
  }

  public Set<Contact> getToRecipients() {
    return toRecipients;
  }

  public void setToRecipients(Set<Contact> toRecipients) {
    this.toRecipients = toRecipients;
  }

  public Set<Contact> getCcRecipients() {
    return ccRecipients;
  }

  public void setCcRecipients(Set<Contact> ccRecipients) {
    this.ccRecipients = ccRecipients;
  }

  public Set<Contact> getBccRecipients() {
    return bccRecipients;
  }

  public void setBccRecipients(Set<Contact> bccRecipients) {
    this.bccRecipients = bccRecipients;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Set<File> getFiles() {
    return files;
  }

  public void setFiles(Set<File> files) {
    this.files = files;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  public EmailConversation getEmailConversation() {
    return emailConversation;
  }

  public void setEmailConversation(EmailConversation emailConversation) {
    this.emailConversation = emailConversation;
  }

  @Override
  public String toString() {
    return "Email{"
        + "from="
        + from
        + ", createdDate="
        + createdDate
        + ", serverId='"
        + serverId
        + '\''
        + ", toRecipients="
        + toRecipients
        + ", ccRecipients="
        + ccRecipients
        + ", bccRecipients="
        + bccRecipients
        + ", subject='"
        + subject
        + '\''
        + ", body='"
        + body
        + '\''
        + ", files="
        + files
        + ", tags="
        + tags
        + ", conversation="
        + conversation
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Email email = (Email) o;

    if (from != null ? !from.equals(email.from) : email.from != null) return false;
    return createdDate != null ? createdDate.equals(email.createdDate) : email.createdDate == null;
  }

  @Override
  public int hashCode() {
    int result = from != null ? from.hashCode() : 0;
    result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
    return result;
  }
}
