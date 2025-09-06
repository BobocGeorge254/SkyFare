package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.entities.UserProfile;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final UserProfileRepository userProfileRepository;
    private final BookRepository bookRepository;

    public UserProfile addBookToWishlist(Long userProfileId, Long bookId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + userProfileId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));

        userProfile.getWishlist().add(book);

        return userProfileRepository.save(userProfile);
    }

    public UserProfile removeBookFromWishlist(Long userProfileId, Long bookId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id " + userProfileId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));

        userProfile.getWishlist().remove(book);

        return userProfileRepository.save(userProfile);
    }

    public Set<Book> getWishlistBooks(Long userProfileId) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));
        return profile.getWishlist();
    }

}
