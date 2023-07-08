package com.epam.esm.securityjwt;

import com.epam.esm.UserRepository;
import com.epam.esm.exception.model.UserNotFoundException;
import com.epam.esm.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may be case-sensitive, or case-insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param eMail the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(eMail)
                .orElseThrow(() -> {
                    log.error("[UserDetailsService.loadUserByUsername()] User with given eMail: [{}] not found", eMail);
                    return new UserNotFoundException(String.format("User not found (eMail:[%s])", eMail));
                });

        log.debug("[UserDetailsService.loadUserByUsername()] User:[{}] with eMail:[{}} has been received.", user, eMail);
        return user;
    }

}
