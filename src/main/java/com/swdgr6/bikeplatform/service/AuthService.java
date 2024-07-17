package com.swdgr6.bikeplatform.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.swdgr6.bikeplatform.model.payload.dto.LoginDto;
import com.swdgr6.bikeplatform.model.payload.dto.SignupDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    AuthenticationResponse login(LoginDto loginDto);
    String signup(SignupDto signupDto);
    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    AuthenticationResponse authenticateWithGoogle(String idToken) throws IOException, FirebaseAuthException;
}
