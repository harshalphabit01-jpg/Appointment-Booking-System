package com.Appointment_Booking_system.service;

import com.Appointment_Booking_system.DTO.LoginResponseDTO;
import com.Appointment_Booking_system.config.JwtUtil;
import com.Appointment_Booking_system.model.DeviceINFO;
import com.Appointment_Booking_system.model.RoleStatus;
import com.Appointment_Booking_system.model.User;
import com.Appointment_Booking_system.repository.DeviceRepo;
import com.Appointment_Booking_system.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepository;
    private final DeviceRepo deviceRepo;
    private final JwtUtil jwtUtil;

    public ResponseEntity<LoginResponseDTO> hendalOAuth2LoginRequest(
            OAuth2User oAuth2User,
            String registrationId
    ) {

        String provider = registrationId.toUpperCase(); // GOOGLE
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        String firstName = fullName != null ? fullName.split(" ")[0] : null;
        String lastName = fullName != null && fullName.split(" ").length > 1
                ? fullName.split(" ")[1]
                : null;


        Optional<User> optionalUser =
                userRepository.findByProviderAndProviderId(provider, providerId);

        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            // SIGNUP
            Optional<User> emailUser = userRepository.findByEmail(email);

            if (emailUser.isPresent()) {
                // User exists with manual registration
                // Link OAuth2 to existing account
                user = emailUser.get();
                user.setProvider(provider);
                user.setProviderId(providerId);
                userRepository.save(user);
            } else {
                // New user - SIGNUP with OAuth2
                user = User.builder()
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .provider(provider)
                        .providerId(providerId)
                        .role(RoleStatus.PATIENT) // default role
                        .password(null) // No password for OAuth2 users
                        .build();

                user = userRepository.save(user);

                // Create DeviceINFO for OAuth2 user (already verified)
                DeviceINFO deviceINFO = new DeviceINFO();
                deviceINFO.setOtp(null);
                deviceINFO.setToken(null);
                deviceINFO.setFlag(true); // OAuth2 users are auto-verified
                deviceINFO.setDoctor(user);
                deviceRepo.save(deviceINFO);
            }
        }

        // Get or create device info
        Optional<DeviceINFO> deviceOpt = deviceRepo.findByDoctor(user);
        DeviceINFO deviceINFO;

        if (deviceOpt.isPresent()) {
            deviceINFO = deviceOpt.get();
        } else {
            // Create device info if doesn't exist
            deviceINFO = new DeviceINFO();
            deviceINFO.setDoctor(user);
            deviceINFO.setFlag(true);
            deviceINFO.setOtp(null);
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", user.getEmail());
        payload.put("role", user.getRole());
        payload.put("provider", user.getProvider());
        String token = jwtUtil.generateToken(payload);

        // Update device with new token
        deviceINFO.setToken(token);
        deviceRepo.save(deviceINFO);


        return ResponseEntity.ok(
                new LoginResponseDTO(
                        "OAuth2 login successful",
                        true,
                        token
                )
        );
    }
}
