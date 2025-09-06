package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.model.entities.Review;
import org.example.skyfarebackend.model.entities.UserProfile;
import org.example.skyfarebackend.model.dto.review.ReviewResponse;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.ReviewRepository;
import org.example.skyfarebackend.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserProfileRepository userProfileRepository;

    public Review createReview(Long bookId, Long userProfileId, int rating, String comment) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));

        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + userProfileId));

        Review review = Review.builder()
                .book(book)
                .userProfile(userProfile)
                .rating(rating)
                .comment(comment)
                .build();

        return reviewRepository.save(review);
    }

    public List<ReviewResponse> getReviewsForBook(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ReviewResponse> getReviewsByUser(Long userProfileId) {
        return reviewRepository.findByUserProfileId(userProfileId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .firstName(review.getUserProfile().getUser().getFirstName())
                .lastName(review.getUserProfile().getUser().getLastName())
                .build();
    }
}
