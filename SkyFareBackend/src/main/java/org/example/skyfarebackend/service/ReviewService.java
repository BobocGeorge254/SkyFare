package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.author.AuthorResponse;
import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.dto.user.UserResponse;
import org.example.skyfarebackend.model.dto.userprofile.UserProfileResponse;
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

    public ReviewResponse createReview(Long bookId, Long userProfileId, int rating, String comment) {
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

        Review saved = reviewRepository.save(review);
        return mapToDto(saved);
    }

    public List<ReviewResponse> getReviewsForBook(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ReviewResponse> getReviewsByUser(Long userProfileId) {
        return reviewRepository.findByUserProfileId(userProfileId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public ReviewResponse mapToDto(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userProfile(UserProfileResponse.builder()
                        .id(review.getUserProfile().getId())
                        .avatarUrl(review.getUserProfile().getAvatarUrl())
                        .user(UserResponse.builder()
                                .id(review.getUserProfile().getUser().getId())
                                .firstName(review.getUserProfile().getUser().getFirstName())
                                .lastName(review.getUserProfile().getUser().getLastName())
                                .email(review.getUserProfile().getUser().getEmail())
                                .build())
                        .build())
                .book(BookResponse.builder()
                        .id(review.getBook().getId())
                        .title(review.getBook().getTitle())
                        .imageUrl(review.getBook().getImageUrl())
                        .author(AuthorResponse.builder()
                                .id(review.getBook().getAuthor().getId())
                                .name(review.getBook().getAuthor().getName())
                                .imageUrl(review.getBook().getAuthor().getImageUrl())
                                .build())
                        .build())
                .build();
    }


}
