package com.swdgr6.bikeplatform.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.swdgr6.bikeplatform.model.entity.AccessToken;
import com.swdgr6.bikeplatform.model.entity.RefreshToken;
import com.swdgr6.bikeplatform.model.entity.Role;
import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.model.exception.BikeApiException;
import com.swdgr6.bikeplatform.model.payload.dto.LoginDto;
import com.swdgr6.bikeplatform.model.payload.dto.SignupDto;
import com.swdgr6.bikeplatform.model.payload.responeModel.AuthenticationResponse;
import com.swdgr6.bikeplatform.repository.AccessTokenRepository;
import com.swdgr6.bikeplatform.repository.RefreshTokenRepository;
import com.swdgr6.bikeplatform.repository.RoleRepository;
import com.swdgr6.bikeplatform.repository.UserRepository;
import com.swdgr6.bikeplatform.security.JwtTokenProvider;
import com.swdgr6.bikeplatform.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private ModelMapper modelMapper;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthenticationResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail())
                .orElseThrow(() -> new BikeApiException(HttpStatus.BAD_REQUEST, "User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(authentication, user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication, user);

        String fullName = user.getFullName();

        revokeRefreshToken(accessToken);
        RefreshToken savedRefreshToken = saveUserRefreshToken(refreshToken);

        revokeAllUserAccessTokens(user);
        saveUserAccessToken(user, accessToken, savedRefreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .fullName(fullName)
                .build();
    }

    public void revokeRefreshToken(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken);
        if (token != null) {
            RefreshToken refreshToken = token.getRefreshToken();
            refreshToken.setRevoked(true);
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);
        }
    }

    public void revokeAllUserAccessTokens(User user) {
        var validUserTokens = accessTokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(accessToken -> {
            accessToken.setRevoked(true);
            accessToken.setExpired(true);
        });
        accessTokenRepository.saveAll(validUserTokens);
    }

    private void saveUserAccessToken(User user, String jwtToken, RefreshToken refreshToken) {
        var token = AccessToken.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(refreshToken)
                .revoked(false)
                .expired(false)
                .build();
        accessTokenRepository.save(token);
    }

    private RefreshToken saveUserRefreshToken(String jwtToken) {
        var token = RefreshToken.builder()
                .token(jwtToken)
                .revoked(false)
                .expired(false)
                .build();
        return refreshTokenRepository.save(token);
    }

    @Override
    public String signup(SignupDto signupDto) {

        // add check if username already exists
        if (userRepository.existsByUsername(signupDto.getUsername())) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Username is already exist!");
        }

        // add check if email already exists
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new BikeApiException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }

        User user = modelMapper.map(signupDto, User.class);

        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        Role userRole = roleRepository.findByRoleName("ROLE_CUSTOMER")
                .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "User Role not found."));
        user.setRole(userRole);
        user.setAvatarUrl("default");

        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        refreshToken = authHeader.substring(7);
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
        username = jwtTokenProvider.getUsernameFromJwt(refreshToken);

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Fetch the User entity
            User user = this.userRepository.findByUsernameOrEmail(username, username)
                    .orElseThrow(() -> new BikeApiException(HttpStatus.BAD_REQUEST, "User not found"));

            if (!token.isRevoked() && !token.isExpired()) {
                // Map user to authentication
                Authentication userAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Generate access token with user details
                String accessToken = jwtTokenProvider.generateAccessToken(userAuthentication, user);

                // Revoke previous access tokens and save the new one
                revokeAllUserAccessTokens(user);
                saveUserAccessToken(user, accessToken, token);

                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            } else {
                throw new BikeApiException(HttpStatus.BAD_REQUEST, "Invalid refresh token");
            }
        }
        return null;
    }

    @Override
    public String authenticateWithGoogle(String idToken) throws IOException, FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();

        if(email == null){
            throw new IOException("Email is required");
        }

        // add check if user already exists
        User user = userRepository.findByUid(uid).orElse(null);

        if(user == null){
            user = new User();
            user.setEmail(decodedToken.getEmail());
            user.setUid(uid);
            user.setAddress("Ho Chi Minh City");
            user.setAvatarUrl("https://www.pngarts.com/files/5/Cartoon-Avatar-PNG-Image-Transparent.png");
            user.setFullName("default");
            user.setDob(LocalDate.now());
            user.setUsername("default");
            user.setGender("default");
            user.setPhone("default");
            user.setPassword(passwordEncoder.encode("default"));
            Role userRole = roleRepository.findByRoleName("ROLE_CUSTOMER")
                    .orElseThrow(() -> new BikeApiException(HttpStatus.NOT_FOUND, "User Role not found."));
            user.setRole(userRole);
            user.setDelete(false);
            userRepository.save(user);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);
        String token = jwtTokenProvider.generateAccessToken(authentication, user);


        return token;
    }
}
