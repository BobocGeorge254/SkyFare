package org.example.skyfarebackend.model.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookUpdate {
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    private Long authorId;
    private Long categoryId;
    private MultipartFile image;
}
