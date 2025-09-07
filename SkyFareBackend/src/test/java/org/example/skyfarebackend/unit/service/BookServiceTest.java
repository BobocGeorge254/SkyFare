package org.example.skyfarebackend.unit.service;

import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.entities.Author;
import org.example.skyfarebackend.model.entities.Book;
import org.example.skyfarebackend.model.entities.Category;
import org.example.skyfarebackend.repository.AuthorRepository;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.CategoryRepository;
import org.example.skyfarebackend.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        authorRepository = mock(AuthorRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        bookService = new BookService(bookRepository, authorRepository, categoryRepository);
    }

    @Test
    void createBook_shouldSaveBookAndReturnDTO() throws IOException {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Category category = new Category();
        category.setId(1L);
        category.setName("Category Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Book savedBook = Book.builder()
                .id(1L)
                .title("Book Title")
                .author(author)
                .category(category)
                .imageUrl(null)
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponse response = bookService.createBook("Book Title", 1L, 1L, null);

        assertNotNull(response);
        assertEquals("Book Title", response.getTitle());
        assertEquals("Author Name", response.getAuthor().getName());
    }

    @Test
    void updateBook_shouldUpdateTitleAuthorAndCategory() throws IOException {
        Author oldAuthor = new Author();
        oldAuthor.setId(1L);
        oldAuthor.setName("Old Author");

        Category oldCategory = new Category();
        oldCategory.setId(1L);
        oldCategory.setName("Old Category");

        Book existingBook = Book.builder()
                .id(1L)
                .title("Old Title")
                .author(oldAuthor)
                .category(oldCategory)
                .build();

        Author newAuthor = new Author();
        newAuthor.setId(2L);
        newAuthor.setName("New Author");

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("New Category");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(newAuthor));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));

        BookResponse updated = bookService.updateBook(1L, "New Title", 2L, 2L, null);

        assertEquals("New Title", updated.getTitle());
        assertEquals("New Author", updated.getAuthor().getName());
        assertEquals("New Category", updated.getCategory().getName());
    }

    @Test
    void deleteBook_shouldRemoveBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.deleteBook(1L);

        verify(bookRepository).delete(book);
    }

    @Test
    void getBookById_shouldReturnCorrectBook() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Category category = new Category();
        category.setId(1L);
        category.setName("Category Name");

        Book book = Book.builder()
                .id(1L)
                .title("Sample Book")
                .author(author)
                .category(category)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertNotNull(response);
        assertEquals("Sample Book", response.getTitle());
        assertEquals("Author Name", response.getAuthor().getName());
        assertEquals("Category Name", response.getCategory().getName());
    }

    @Test
    void getAllBooks_shouldReturnPaginatedResults() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author");

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");

        Book book1 = Book.builder().id(1L).title("Book A").author(author).category(category).build();
        Book book2 = Book.builder().id(2L).title("Book B").author(author).category(category).build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
        Page<Book> page = new PageImpl<>(List.of(book1, book2), pageable, 2);

        when(bookRepository.findAll(pageable)).thenReturn(page);

        Page<BookResponse> result = bookService.getAllBooks(0, 10, "title", "asc", null, null);

        assertEquals(2, result.getTotalElements());
        assertEquals("Book A", result.getContent().get(0).getTitle());
        assertEquals("Book B", result.getContent().get(1).getTitle());
    }

    @Test
    void getAllBooks_filterByAuthorAndCategory() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author");

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");

        Book book = Book.builder()
                .id(1L)
                .title("Filtered Book")
                .author(author)
                .category(category)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findByAuthorIdAndCategoryId(1L, 1L, pageable)).thenReturn(page);

        Page<BookResponse> result = bookService.getAllBooks(0, 10, "title", "asc", 1L, 1L);

        assertEquals(1, result.getTotalElements());
        assertEquals("Filtered Book", result.getContent().get(0).getTitle());
    }
}