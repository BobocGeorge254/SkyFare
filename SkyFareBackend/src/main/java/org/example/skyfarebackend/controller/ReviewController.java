package org.example.skyfarebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.review.ReviewCreate;
import org.example.skyfarebackend.model.dto.review.ReviewResponse;
import org.example.skyfarebackend.model.entities.Review;
import org.example.skyfarebackend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid ReviewCreate request) {
        return ResponseEntity.ok(reviewService.createReview(request.getBookId(), request.getUserProfileId(), request.getRating(), request.getComment()));
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
