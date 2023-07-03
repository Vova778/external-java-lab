package com.epam.esm;

import com.epam.esm.jwt.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByJwt(String jwt);

    @Query("SELECT t FROM User u JOIN" +
            " u.tokens t WHERE u.id = (:userID) and (t.expired = false or t.revoked = false) ORDER BY t.id")
    List<Token> findAllValidByUser(Long userID);

    boolean existsByJwt(String jwt);
}
