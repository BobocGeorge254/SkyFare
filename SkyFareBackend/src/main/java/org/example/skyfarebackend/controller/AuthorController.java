package org.example.skyfarebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.author.AuthorCreate;
import org.example.skyfarebackend.model.dto.author.AuthorUpdate;
import org.example.skyfarebackend.model.entities.Author;
import org.example.skyfarebackend.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid AuthorCreate request) throws IOException {
        return ResponseEntity.ok(authorService.createAuthor(request.getName(), request.getImage()));
    }


    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @PathVariable Long id,
            @Valid AuthorUpdate request
    ) throws IOException {
        return ResponseEntity.ok(authorService.updateAuthor(id, request.getName(), request.getImage()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
