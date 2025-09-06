package org.example.skyfarebackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.review.ReviewResponse;
import org.example.skyfarebackend.model.entities.Review;
import org.example.skyfarebackend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @RequestParam Long bookId,
            @RequestParam Long userProfileId,
            @RequestParam int rating,
            @RequestParam String comment
    ) {
        return ResponseEntity.ok(reviewService.createReview(bookId, userProfileId, rating, comment));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsForBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsForBook(bookId));
    }

    @GetMapping("/user/{userProfileId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userProfileId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
