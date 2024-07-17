package com.swdgr6.bikeplatform.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.swdgr6.bikeplatform.model.payload.dto.LoginDto;
import com.swdgr6.bikeplatform.model.payload.dto.SignupDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.AuthenticationResponse;
import com.swdgr6.bikeplatform.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/no-auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginDto loginDto){
        AuthenticationResponse token = authService.login(loginDto);
        return  ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupDto signupDto){
        String response = authService.signup(signupDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @PostMapping("/login-google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> tokenRequest) {
        String idToken = tokenRequest.get("idToken");
        try {
            AuthenticationResponse token = authService.authenticateWithGoogle(idToken);
            return ResponseEntity.ok(token);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Authentication failed: Invalid Firebase token");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

}
