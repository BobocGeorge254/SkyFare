package org.example.skyfarebackend.repository;

import org.example.skyfarebackend.model.entities.User;
import org.example.skyfarebackend.model.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
}
