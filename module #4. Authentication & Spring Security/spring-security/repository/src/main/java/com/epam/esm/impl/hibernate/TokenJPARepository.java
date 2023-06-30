package com.epam.esm.impl.hibernate;

import com.epam.esm.TokenRepository;
import com.epam.esm.jwt.Token;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TokenJPARepository implements TokenRepository {

    private static final String FIND_ALL_VALID_BY_USER = "SELECT t FROM User u JOIN" +
            " u.tokens t WHERE u.id = (:id) and (t.expired = false or t.revoked = false) ORDER BY t.id";

    private static final String FIND_BY_VALUE = "SELECT t FROM Token t WHERE t.jwt = (:token)";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isExists(Token object) {
        return findByToken(object.getJwt()).isPresent();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Token save(Token token) {
        return entityManager.merge(token);
    }


    public Optional<Token> findByID(Long id) {
        Token token = entityManager.find(Token.class, id);
        return Optional.ofNullable(token);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        Optional<Token> result;
        try {
            result = Optional.ofNullable(entityManager
                    .createQuery(FIND_BY_VALUE, Token.class)
                    .setParameter("token", token)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.error("[TokenJPARepository.findByToken()] NoResultException, Optional.empty() returned!!!");
            return Optional.empty();
        }
        return result;
    }

    @Override
    public List<Token> findAllValidByUser(Long userID) {
        return entityManager
                .createQuery(FIND_ALL_VALID_BY_USER, Token.class)
                .setParameter("id", userID)
                .getResultList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Token deleteByID(Long id) {
        Token token = entityManager.find(Token.class, id);
        log.debug("Token for removal {}", token);
        entityManager.remove(token);
        return token;
    }

}