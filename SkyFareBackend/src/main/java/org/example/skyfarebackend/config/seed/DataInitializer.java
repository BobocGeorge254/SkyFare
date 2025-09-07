package org.example.skyfarebackend.config.seed;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.entities.User;
import org.example.skyfarebackend.model.enums.Role;
import org.example.skyfarebackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@skyfare.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@skyfare.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("System")
                    .lastName("Admin")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
