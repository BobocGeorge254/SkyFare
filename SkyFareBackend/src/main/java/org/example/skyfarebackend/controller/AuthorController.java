package org.example.skyfarebackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.author.AuthorRequest;
import org.example.skyfarebackend.model.Author;
import org.example.skyfarebackend.service.AuthorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public Author createAuthor(@RequestParam("name") String name,
             @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        return authorService.createAuthor(name, imageFile);
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }
}
