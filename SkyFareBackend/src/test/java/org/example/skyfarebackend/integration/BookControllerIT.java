package org.example.skyfarebackend.integration;

import org.example.skyfarebackend.model.entities.Author;
import org.example.skyfarebackend.model.entities.Category;
import org.example.skyfarebackend.repository.AuthorRepository;
import org.example.skyfarebackend.repository.BookRepository;
import org.example.skyfarebackend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.yml")
@AutoConfigureMockMvc
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Author author;
    private Category category;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();

        author = new Author();
        author.setName("Integration Author");
        author = authorRepository.save(author);

        category = new Category();
        category.setName("Integration Category");
        category = categoryRepository.save(category);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_shouldPersistAndReturnBook() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy-image".getBytes()
        );

        mockMvc.perform(multipart("/api/books")
                        .file(image)
                        .param("title", "Integration Test Book")
                        .param("authorId", author.getId().toString())
                        .param("categoryId", category.getId().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Book"))
                .andExpect(jsonPath("$.author.name").value("Integration Author"))
                .andExpect(jsonPath("$.category.name").value("Integration Category"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllBooks_shouldReturnBooks() throws Exception {
        mockMvc.perform(multipart("/api/books")
                        .param("title", "Preloaded Book")
                        .param("authorId", author.getId().toString())
                        .param("categoryId", category.getId().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Preloaded Book"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getBookById_shouldReturnBook() throws Exception {
        String response = mockMvc.perform(multipart("/api/books")
                        .param("title", "FindMe")
                        .param("authorId", author.getId().toString())
                        .param("categoryId", category.getId().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String id = response.replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("FindMe"));
    }
}
