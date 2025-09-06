package org.example.skyfarebackend.repository;

import org.example.skyfarebackend.model.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    List<Review> findByUserProfileId(Long userProfileId);
}
