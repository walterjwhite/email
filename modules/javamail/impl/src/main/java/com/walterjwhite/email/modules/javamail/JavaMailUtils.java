package com.walterjwhite.email.modules.javamail;

import com.walterjwhite.email.api.model.PrivateEmailAccount;
import com.walterjwhite.email.modules.javamail.authentication.PasswordAuthenticator;
import java.util.Properties;
import javax.mail.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JavaMailUtils {
  private static Authenticator getAuthenticator(PrivateEmailAccount privateEmailAccount) {
    return (new PasswordAuthenticator(
        new PasswordAuthentication(
            privateEmailAccount.getUsername(), privateEmailAccount.getPassword().getPlainText())));
  }

  public static Properties getProperties(PrivateEmailAccount privateEmailAccount) {
    final Properties properties = new Properties();

    for (final String key : privateEmailAccount.getProvider().getSettings().keySet()) {
      properties.setProperty(key, privateEmailAccount.getProvider().getSettings().get(key));
    }

    return (properties);
  }

  public static Session getSession(PrivateEmailAccount privateEmailAccount) {
    return (Session.getInstance(
        getProperties(privateEmailAccount), getAuthenticator(privateEmailAccount)));
  }

  public static Store getStore(Session session) throws MessagingException {
    Store store = session.getStore();
    if (!store.isConnected()) {
      store.connect();
    }

    return (store);
  }
}
