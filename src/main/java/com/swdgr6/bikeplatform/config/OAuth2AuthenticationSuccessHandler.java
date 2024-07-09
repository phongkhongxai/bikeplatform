package com.swdgr6.bikeplatform.config;

import com.swdgr6.bikeplatform.model.entity.Role;
import com.swdgr6.bikeplatform.model.entity.User;
import com.swdgr6.bikeplatform.repository.RoleRepository;
import com.swdgr6.bikeplatform.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
            logger.info("User successfully authenticated");

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
//
//        logger.info(user.getName());
//
//        user.getAttributes().forEach((k, v) -> {
//            logger.info("{} => {}", k, v);
//        });
//
//        logger.info(user.getAttributes().toString());

        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("avatarUrl").toString();

        Role role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        //create user and save in database
        User user1 = new User();
        user1.setEmail(email);
        user1.setFullName(name);
        user1.setAvatarUrl(picture);
        user1.setPassword("password");
        user1.setUsername(email);
        user1.setRole(role);

        User user2 = userRepository.findByEmail(email).orElse(null);
        if (user2 == null) {
            userRepository.save(user1);
            logger.info("User saved: " + email);
        }

            new DefaultRedirectStrategy().sendRedirect(request, response, "/swagger-ui.html");
    }
}