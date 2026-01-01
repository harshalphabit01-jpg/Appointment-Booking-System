package com.Appointment_Booking_system.config;

import com.Appointment_Booking_system.DTO.LoginResponseDTO;
import com.Appointment_Booking_system.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String registrationId = token.getAuthorizedClientRegistrationId();

        try {
            // Handle OAuth2 login/signup - call only ONCE
            ResponseEntity<LoginResponseDTO> loginResponse =
                    authService.hendalOAuth2LoginRequest(oAuth2User, registrationId);

            LoginResponseDTO body = loginResponse.getBody();

            if (body != null && body.isSuccess()) {
                String jwt = body.getToken();

                // Redirect to frontend with token
                String redirectUrl = "http://localhost:5173/oauth2/redirect?token=" +
                        URLEncoder.encode(jwt, StandardCharsets.UTF_8);

                response.sendRedirect(redirectUrl);
                log.info("OAuth2 login successful for user: {}", (Object) oAuth2User.getAttribute("email"));
            } else {
                // Handle error
                response.sendRedirect("http://localhost:5173/login?error=oauth2_failed");
                log.error("OAuth2 login failed");
            }

        } catch (Exception e) {
            log.error("Error during OAuth2 authentication: {}", e.getMessage());
            response.sendRedirect("http://localhost:5173/login?error=oauth2_error");
        }
    }
}