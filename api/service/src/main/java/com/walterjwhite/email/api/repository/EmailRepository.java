package com.walterjwhite.email.api.repository;

import com.walterjwhite.datastore.criteria.AbstractRepository;
import com.walterjwhite.email.api.model.Email;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

public class EmailRepository extends AbstractRepository<Email> {

  @Inject
  public EmailRepository(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
    super(entityManager, criteriaBuilder, Email.class);
  }

  //  public Set<Email> findByFrom(final Person person){
  //
  //  }
  //
  //  Set<Email> findByTagsContaining(final Tag tag);
  //
  //  Set<Email> findBySubjectContaining(final String subjectExcerpt);
  //
  //  Set<Email> findBySentDateBetween(final LocalDateTime startRange, final LocalDateTime
  // endRange);
}
