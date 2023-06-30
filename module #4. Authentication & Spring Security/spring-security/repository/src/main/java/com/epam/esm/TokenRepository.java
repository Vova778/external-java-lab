package com.epam.esm;

import com.epam.esm.jwt.Token;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository  {
    boolean isExists(Token token);

    Token save(Token token);

    Optional<Token> findByToken(String token);

    List<Token> findAllValidByUser(Long userID);

    Token deleteByID(Long id);
}
