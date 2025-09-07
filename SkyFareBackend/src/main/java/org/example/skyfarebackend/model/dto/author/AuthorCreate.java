package org.example.skyfarebackend.model.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AuthorCreate {

    @NotBlank(message = "Author name is required")
    private String name;

    private MultipartFile image;
}
