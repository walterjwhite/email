package com.walterjwhite.data.pipe.modules.email.javamail.sink;

import com.google.inject.persist.Transactional;
import com.walterjwhite.datastore.modules.guice.persist.service.FieldRefresherUtil;
import com.walterjwhite.email.api.model.Email;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Provider;

public class EmailJPASink implements Consumer<Email>, AutoCloseable {
  protected final Provider<EntityManager> entityManagerProvider;

  @Inject
  public EmailJPASink(Provider<EntityManager> entityManagerProvider) {
    this.entityManagerProvider = entityManagerProvider;
  }

  @Override
  public void close() throws Exception {}

  @Transactional
  @Override
  public synchronized void accept(Email email) {
    final EntityManager entityManager = entityManagerProvider.get();
    try {
      email = (Email) FieldRefresherUtil.refresh(entityManager, email, true);
      entityManager.getTransaction().begin();
      entityManager.merge(email);
      entityManager.getTransaction().commit();
    } catch (IllegalAccessException e) {
      entityManager.getTransaction().rollback();
      throw new RuntimeException("Error refreshing entity", e);
    } finally {
      // entityManager.close();
    }
  }
}
