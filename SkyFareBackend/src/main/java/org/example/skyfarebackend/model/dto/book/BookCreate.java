package org.example.skyfarebackend.model.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookCreate {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    private Long authorId;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private MultipartFile image;
}
