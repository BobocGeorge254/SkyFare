package org.example.skyfarebackend.model.dto.author;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AuthorUpdate {

    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters")
    private String name;

    private MultipartFile image;
}

