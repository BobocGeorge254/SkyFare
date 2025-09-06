package org.example.skyfarebackend.repository;

import org.example.skyfarebackend.model.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
