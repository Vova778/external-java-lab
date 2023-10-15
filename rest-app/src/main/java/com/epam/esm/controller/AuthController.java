package com.epam.esm.controller;

import com.epam.esm.payload.request.AuthRequest;
import com.epam.esm.payload.request.SignUpRequest;
import com.epam.esm.payload.response.AuthenticationResponse;
import com.epam.esm.securityjwt.AuthService;
import com.epam.esm.securityjwt.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final LogoutService logoutService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Привіт";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        // Create new user's account
        log.debug("[AuthController.registerUser()] Sign-Up request: [{}}", signUpRequest);

        AuthenticationResponse authenticationResponse = authService.signUp(signUpRequest);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        log.debug("[AuthController.registerUser()] Sign-In request: [{}}", authRequest);

        AuthenticationResponse authenticationResponse =
                authService.signIn(authRequest);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {

        AuthenticationResponse authenticationResponse =
                authService.refreshToken(request);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(httpServletRequest, httpServletResponse, null);
    }
}