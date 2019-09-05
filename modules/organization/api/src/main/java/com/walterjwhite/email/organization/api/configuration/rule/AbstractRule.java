package com.walterjwhite.email.organization.api.configuration.rule;

import com.walterjwhite.email.api.model.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// @TODO: reevaluate which fields are included for .equals and .hashCode
// was originally email message field, but that is rather limiting
// consider all fields?
// this is required for serialization and deserialization to ensure that subclasses are properly
// read
// @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property="@class")
@Data
@ToString(doNotUseGetters = true)
public abstract class AbstractRule {

  @EqualsAndHashCode.Exclude protected boolean invert;

  public abstract boolean matches(final Email email);
}
