package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.entities.Author;
import org.example.skyfarebackend.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author createAuthor(String name, MultipartFile imageFile) throws IOException {
        String imageUrl = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = imageFile.getOriginalFilename();
            Path uploadDir = Path.of("uploads/images");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, imageFile.getBytes());
            imageUrl = "http://localhost:8080/images/" + filename;
        }

        Author author = Author.builder()
                .name(name)
                .imageUrl(imageUrl)
                .build();
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, String name, MultipartFile imageFile) throws IOException {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + id));

        if (name != null && !name.isBlank()) {
            author.setName(name);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = imageFile.getOriginalFilename();
            Path uploadDir = Path.of("uploads/images");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, imageFile.getBytes());

            String imageUrl = "http://localhost:8080/images/" + filename;
            author.setImageUrl(imageUrl);
        }

        return authorRepository.save(author);
    }


    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + id));
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + id));

        authorRepository.delete(author);
    }

}
