package com.walterjwhite.email.api.model;

import com.walterjwhite.datastore.api.model.entity.AbstractNamedEntity;
import com.walterjwhite.email.api.enumeration.EmailProviderType;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Google, Exchange ... */
@NoArgsConstructor
@Getter
@Setter
@ToString(doNotUseGetters = true, callSuper = true)
// @PersistenceCapable
@Entity
public class EmailProvider extends AbstractNamedEntity {
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  protected EmailProviderType type;

  // if this works, great, otherwise, setup a key-value pair entity
  @ElementCollection @CollectionTable @MapKeyColumn @Column
  protected Map<String, String> settings = new HashMap<>();

  public EmailProvider(
      String name, String description, EmailProviderType type, Map<String, String> settings) {
    super(name, description);
    this.type = type;

    if (settings != null && !settings.isEmpty()) this.settings.putAll(settings);
  }
}
