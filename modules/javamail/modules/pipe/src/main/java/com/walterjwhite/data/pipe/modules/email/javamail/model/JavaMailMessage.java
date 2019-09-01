package com.walterjwhite.data.pipe.modules.email.javamail.model;

import java.io.Serializable;
import java.util.Objects;
import javax.mail.Folder;
import javax.mail.Message;

public class JavaMailMessage implements Serializable {
  protected final Message message;
  protected final Folder folder;

  public JavaMailMessage(Message message, Folder folder) {
    this.message = message;
    this.folder = folder;
  }

  public Folder getFolder() {
    return folder;
  }

  public Message getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JavaMailMessage that = (JavaMailMessage) o;
    return Objects.equals(message, that.message) && Objects.equals(folder, that.folder);
  }

  @Override
  public int hashCode() {

    return Objects.hash(message, folder);
  }
}
