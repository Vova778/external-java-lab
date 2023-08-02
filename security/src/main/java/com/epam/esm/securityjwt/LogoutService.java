package com.epam.esm.securityjwt;


import com.epam.esm.TokenRepository;
import com.epam.esm.exception.model.TokenNotFoundException;
import com.epam.esm.jwt.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    /**
     * Causes a logout to be completed. The method must complete successfully.
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the current principal details
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String jwt = authHeader.substring(7);

        Token storedToken = tokenRepository.findByJwt(jwt)
                .orElseThrow(() -> {
                    log.error("[LogoutService.logout()] Token with given value:[{}] not found", jwt);
                    return new TokenNotFoundException(String.format("Token not found (value:[%s])", jwt));
                });

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);

            tokenRepository.save(storedToken);

            SecurityContextHolder.clearContext();
        }
    }
}