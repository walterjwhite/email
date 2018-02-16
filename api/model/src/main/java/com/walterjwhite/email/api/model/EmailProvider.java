package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import com.walterjwhite.email.api.enumeration.EmailProviderType;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

/** Google, Exchange ... */
@Entity
public class EmailProvider extends AbstractNamedEntity {
  @Enumerated(EnumType.STRING)
  @Column
  protected EmailProviderType type;

  // if this works, great, otherwise, setup a key-value pair entity
  @ElementCollection @CollectionTable @MapKeyColumn @Column
  protected Map<String, String> settings = new HashMap<>();

  //    properties.setProperty("mail.smtp.host", "");

  //    properties.put("mail.smtp.host", "smtp.gmail.com");
  //    properties.put("mail.smtp.socketFactory.port", "465");
  //    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
  //    properties.put("mail.smtp.auth", "true");
  //    properties.put("mail.smtp.port", "465");

  public EmailProvider(
      String name, String description, EmailProviderType type, Map<String, String> settings) {
    super(name, description);
    this.type = type;

    this.settings.putAll(settings);
  }

  public EmailProviderType getType() {
    return type;
  }

  public void setType(EmailProviderType type) {
    this.type = type;
  }

  public Map<String, String> getSettings() {
    return settings;
  }

  public void setSettings(Map<String, String> settings) {
    this.settings = settings;
  }
}
