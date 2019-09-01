package com.walterjwhite.email.api.annotation;

import com.walterjwhite.email.api.enumeration.EmailProviderType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EmailProvider {
  EmailProviderType value();
}
