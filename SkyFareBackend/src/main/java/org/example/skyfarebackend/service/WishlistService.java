package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.author.AuthorResponse;
import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.dto.userprofile.UserProfileResponse;
import org.example.skyfarebackend.model.entities.UserProfile;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final UserProfileRepository userProfileRepository;
    private final BookRepository bookRepository;

    public UserProfileResponse addBookToWishlist(Long userProfileId, Long bookId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + userProfileId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));

        userProfile.getWishlist().add(book);
        userProfileRepository.save(userProfile);

        return mapToDto(userProfile);
    }

    public UserProfileResponse removeBookFromWishlist(Long userProfileId, Long bookId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + userProfileId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));

        userProfile.getWishlist().remove(book);
        userProfileRepository.save(userProfile);

        return mapToDto(userProfile);
    }

    public Set<BookResponse> getWishlistBooks(Long userProfileId) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));

        return profile.getWishlist().stream()
                .map(b -> BookResponse.builder()
                        .id(b.getId())
                        .title(b.getTitle())
                        .imageUrl(b.getImageUrl())
                        .author(AuthorResponse.builder()
                                .id(b.getAuthor().getId())
                                .name(b.getAuthor().getName())
                                .imageUrl(b.getAuthor().getImageUrl())
                                .build())
                        .build())
                .collect(Collectors.toSet());
    }


    private UserProfileResponse mapToDto(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .avatarUrl(profile.getAvatarUrl())
                .wishlist(profile.getWishlist().stream()
                        .map(b -> BookResponse.builder()
                                .id(b.getId())
                                .title(b.getTitle())
                                .imageUrl(b.getImageUrl())
                                .author(AuthorResponse.builder()
                                        .id(b.getAuthor().getId())
                                        .name(b.getAuthor().getName())
                                        .imageUrl(b.getAuthor().getImageUrl())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

}
