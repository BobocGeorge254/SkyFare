package org.example.skyfarebackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.dto.userprofile.UserProfileResponse;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.model.entities.UserProfile;
import org.example.skyfarebackend.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{userProfileId}/books/{bookId}")
    public ResponseEntity<UserProfileResponse> addBookToWishlist(
            @PathVariable Long userProfileId,
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(wishlistService.addBookToWishlist(userProfileId, bookId));
    }

    @DeleteMapping("/{userProfileId}/books/{bookId}")
    public ResponseEntity<UserProfileResponse> removeBookFromWishlist(
            @PathVariable Long userProfileId,
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(wishlistService.removeBookFromWishlist(userProfileId, bookId));
    }

    @GetMapping("/{userProfileId}/books")
    public ResponseEntity<Set<BookResponse>> getWishlistBooks(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(wishlistService.getWishlistBooks(userProfileId));
    }

}
