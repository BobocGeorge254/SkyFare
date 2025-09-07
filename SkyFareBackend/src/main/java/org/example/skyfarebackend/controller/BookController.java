package org.example.skyfarebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.book.BookCreate;
import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.dto.book.BookUpdate;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid BookCreate request) throws IOException {
        return ResponseEntity.ok(bookService.createBook(request.getTitle(), request.getAuthorId(), request.getCategoryId(), request.getImage()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid BookUpdate request) throws IOException {
        return ResponseEntity.ok(bookService.updateBook(id, request.getTitle(), request.getAuthorId(), request.getCategoryId(), request.getImage()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(
                bookService.getAllBooks(page, size, sortBy, direction, authorId, categoryId)
        );
    }
}
