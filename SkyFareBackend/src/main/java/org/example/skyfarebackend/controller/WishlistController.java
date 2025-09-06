package org.example.skyfarebackend.controller;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<UserProfile> addBookToWishlist(
            @PathVariable Long userProfileId,
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(wishlistService.addBookToWishlist(userProfileId, bookId));
    }

    @DeleteMapping("/{userProfileId}/books/{bookId}")
    public ResponseEntity<UserProfile> removeBookFromWishlist(
            @PathVariable Long userProfileId,
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(wishlistService.removeBookFromWishlist(userProfileId, bookId));
    }

    @GetMapping("/{userProfileId}/books")
    public ResponseEntity<Set<Book>> getWishlistBooks(@PathVariable Long userProfileId) {
        return ResponseEntity.ok(wishlistService.getWishlistBooks(userProfileId));
    }

}
