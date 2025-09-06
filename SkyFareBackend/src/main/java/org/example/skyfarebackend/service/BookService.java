package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.Author;
import org.example.skyfarebackend.model.Book;
import org.example.skyfarebackend.model.Category;
import org.example.skyfarebackend.repository.AuthorRepository;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public Book createBook(String title, Long authorId, Long categoryId, MultipartFile imageFile) throws IOException {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + authorId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));

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

        Book book = Book.builder()
                .title(title)
                .author(author)
                .category(category)
                .imageUrl(imageUrl)
                .build();

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, String title, Long authorId, Long categoryId, MultipartFile imageFile) throws IOException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));

        if (title != null && !title.isBlank()) {
            book.setTitle(title);
        }

        if (authorId != null) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Author not found with id " + authorId));
            book.setAuthor(author);
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
            book.setCategory(category);
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
            book.setImageUrl(imageUrl);
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
        bookRepository.delete(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    public Page<Book> getAllBooks(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findAll(pageable);
    }
}
