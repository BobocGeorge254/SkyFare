package org.example.skyfarebackend.unit.service;

import org.example.skyfarebackend.model.dto.review.ReviewResponse;
import org.example.skyfarebackend.model.entities.*;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.ReviewRepository;
import org.example.skyfarebackend.repository.UserProfileRepository;
import org.example.skyfarebackend.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private BookRepository bookRepository;
    private UserProfileRepository userProfileRepository;
    private ReviewService reviewService;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        bookRepository = mock(BookRepository.class);
        userProfileRepository = mock(UserProfileRepository.class);
        reviewService = new ReviewService(reviewRepository, bookRepository, userProfileRepository);

        securityContextHolderMock = mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMock.close();
    }

    private User mockCurrentUser(Long id) {
        User user = new User();
        user.setId(id);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        return user;
    }

    @Test
    void createReview_shouldSaveAndReturnDto() {
        User currentUser = mockCurrentUser(1L);

        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setUser(currentUser);

        Book book = new Book();
        book.setId(10L);
        Author author = new Author();
        author.setId(100L);
        author.setName("Author");
        book.setAuthor(author);
        book.setTitle("Book Title");

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));

        Review saved = Review.builder()
                .id(5L)
                .book(book)
                .userProfile(profile)
                .rating(4)
                .comment("Nice book")
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(saved);

        ReviewResponse response = reviewService.createReview(10L, 1L, 4, "Nice book");

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals(4, response.getRating());
        assertEquals("Nice book", response.getComment());
        assertEquals("Book Title", response.getBook().getTitle());
        assertEquals("Author", response.getBook().getAuthor().getName());
    }

    @Test
    void getReviewsForBook_shouldReturnMappedList() {
        Book book = new Book();
        book.setId(10L);
        Author author = new Author();
        author.setId(100L);
        author.setName("Author");
        book.setAuthor(author);
        book.setTitle("Book Title");

        User user = new User();
        user.setId(1L);
        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setUser(user);

        Review review = Review.builder()
                .id(1L)
                .book(book)
                .userProfile(profile)
                .rating(5)
                .comment("Excellent")
                .build();

        when(reviewRepository.findByBookId(10L)).thenReturn(List.of(review));

        List<ReviewResponse> responses = reviewService.getReviewsForBook(10L);

        assertEquals(1, responses.size());
        assertEquals("Excellent", responses.getFirst().getComment());
        assertEquals(5, responses.getFirst().getRating());
    }

    @Test
    void deleteReview_shouldDeleteIfOwner() {
        User currentUser = mockCurrentUser(1L);

        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setUser(currentUser);

        Book book = new Book();
        book.setId(10L);
        Author author = new Author();
        author.setId(100L);
        author.setName("Author");
        book.setAuthor(author);
        book.setTitle("Book Title");

        Review review = Review.builder()
                .id(7L)
                .book(book)
                .userProfile(profile)
                .rating(3)
                .comment("Ok")
                .build();

        when(reviewRepository.findById(7L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(7L);

        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void deleteReview_shouldThrowIfNotOwner() {
        mockCurrentUser(99L); // different user

        User otherUser = new User();
        otherUser.setId(1L);

        UserProfile profile = new UserProfile();
        profile.setId(1L);
        profile.setUser(otherUser);

        Review review = Review.builder()
                .id(8L)
                .userProfile(profile)
                .rating(2)
                .comment("Not yours")
                .build();

        when(reviewRepository.findById(8L)).thenReturn(Optional.of(review));

        assertThrows(RuntimeException.class, () -> reviewService.deleteReview(8L));

        verify(reviewRepository, never()).delete(any());
    }
}
