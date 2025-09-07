package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.auth.LoginResponse;
import org.example.skyfarebackend.model.entities.User;
import org.example.skyfarebackend.model.entities.UserProfile;
import org.example.skyfarebackend.model.enums.Role;
import org.example.skyfarebackend.repository.UserProfileRepository;
import org.example.skyfarebackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(String email, String password, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .firstName(firstName)
                        .lastName(lastName)
                        .role(Role.USER)
                        .build();

        UserProfile profile = UserProfile.builder()
                        .user(user)
                        .avatarUrl(null)
                        .wishlist(new HashSet<>())
                        .build();

        userRepository.save(user);
        userProfileRepository.save(profile);
    }

    public LoginResponse login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        Long userProfileId = null;
        if (user.getRole() == Role.USER) {
            UserProfile userProfile = userProfileRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("User profile not found"));
            userProfileId = userProfile.getId();
        }

        return LoginResponse.builder()
                .token(token)
                .userProfileId(userProfileId)
                .build();
    }

}