package org.example.skyfarebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.skyfarebackend.model.dto.author.AuthorResponse;
import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.dto.category.CategoryResponse;
import org.example.skyfarebackend.model.entities.Author;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.model.entities.Category;
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

    public BookResponse createBook(String title, Long authorId, Long categoryId, MultipartFile imageFile) throws IOException {
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

        Book saved = bookRepository.save(book);
        return mapToDTO(saved);
    }

    public BookResponse updateBook(Long id, String title, Long authorId, Long categoryId, MultipartFile imageFile) throws IOException {
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

        Book saved = bookRepository.save(book);
        return mapToDTO(saved);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
        bookRepository.delete(book);
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
        return mapToDTO(book);
    }

    public Page<BookResponse> getAllBooks(int page, int size, String sortBy, String direction,
                                  Long authorId, Long categoryId) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> books;

        if (authorId != null && categoryId != null) {
            books = bookRepository.findByAuthorIdAndCategoryId(authorId, categoryId, pageable);
        } else if (authorId != null) {
            books =  bookRepository.findByAuthorId(authorId, pageable);
        } else if (categoryId != null) {
            books =  bookRepository.findByCategoryId(categoryId, pageable);
        } else {
            books =  bookRepository.findAll(pageable);
        }

        return books.map(this::mapToDTO);
    }

    private BookResponse mapToDTO(Book book) {
        AuthorResponse authorDTO = AuthorResponse.builder()
                .id(book.getAuthor().getId())
                .name(book.getAuthor().getName())
                .imageUrl(book.getAuthor().getImageUrl())
                .build();

        CategoryResponse categoryDTO = CategoryResponse.builder()
                .id(book.getCategory().getId())
                .name(book.getCategory().getName())
                .build();

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .imageUrl(book.getImageUrl())
                .author(authorDTO)
                .category(categoryDTO)
                .build();
    }


}
