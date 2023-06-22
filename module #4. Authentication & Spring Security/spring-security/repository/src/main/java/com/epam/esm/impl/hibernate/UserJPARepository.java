package com.epam.esm.impl.hibernate;


import com.epam.esm.UserRepository;
import com.epam.esm.model.entity.User;
import com.epam.esm.utils.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Getter
public class UserJPARepository implements UserRepository {
    private static final String FIND_BY_NAME = "SELECT u FROM User u WHERE u.firstName= :name";
    private static final String FIND_BY_EMAIL = "SELECT u FROM User u WHERE u.email = :email";
    private static final String FIND_BY_RECEIPT = "SELECT r.user FROM Receipt r WHERE r.id = :id";
    private static final String FIND_ALL = "SELECT u FROM User u";
    private static final String FIND_ALL_BY_NAME = "SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(:name)";
    private static final String GET_TOTAL_RECORDS = "SELECT COUNT(u.id) from User u";
    private static final String GET_TOTAL_RECORDS_FOR_NAME_LIKE = "SELECT COUNT(u.id) from User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(:name) ";



    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isExists(User object) {
        throw new UnsupportedOperationException();
    }
    public boolean isExistsByEmail(User user) {
        log.debug("[UserJPARepository.isExistsByEmail()] Email for search: [{}]", user.getEmail());
        return findByEmail(user.getEmail()).isPresent();
    }
    @Override
    public Optional<User> findByReceipt(Long receiptID) {
        Optional<User> result;
        try {
            result = Optional.ofNullable(entityManager
                    .createQuery(FIND_BY_RECEIPT, User.class)
                    .setParameter("id", receiptID)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.error("[UserJPARepository.findByReceipt()] NoResultException," +
                    " Optional.empty() has been returned!");
            return Optional.empty();
        }
        return result;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> result;
        log.debug("[UserJPARepository.findByEmail()] Email for search: [{}}", email);
        try {
            result = Optional.ofNullable(entityManager
                    .createQuery(FIND_BY_EMAIL, User.class)
                    .setParameter("email", email)
                    .getSingleResult());

        } catch (NoResultException e) {
            log.error("[UserJPARepository.findByEmail()] NoResultException, Optional.empty() returned!");
            return Optional.empty();
        }
        log.debug("[UserJPARepository.findByEmail()] Optional user result: [{}]", result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }


    @Override
    public Optional<User> findByName(String name) {
        Optional<User> result;
        try {
            TypedQuery<User> query = entityManager.createQuery(FIND_BY_NAME, User.class);
            query.setParameter("name", name);
            result = Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            log.error("[UserJPARepository.findByName()] NoResultException," +
                    " Optional.empty() has been returned!");
            return Optional.empty();
        }
        return result;
    }

    @Override
    public List<User> findAllByName(String name, Pageable pageable) {
        int firstResult = (pageable.getPage() - 1) * pageable.getPageSize();
        TypedQuery<User> query = entityManager.createQuery(
                        FIND_ALL_BY_NAME, User.class)
                .setParameter("name", "%" + name + "%")
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        int firstResult = (pageable.getPage() - 1) * pageable.getPageSize();
        return entityManager.createQuery(FIND_ALL, User.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public User deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getTotalRecords() {
        TypedQuery<Long> countQuery = entityManager.createQuery(GET_TOTAL_RECORDS, Long.class);
        return countQuery.getSingleResult();
    }
    public Long getTotalRecordsForNameLike(String name) {
        return entityManager
                .createQuery(GET_TOTAL_RECORDS_FOR_NAME_LIKE, Long.class)
                .setParameter("name", "%" + name + "%")
                .getSingleResult();
    }
}