package org.example.skyfarebackend.model.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreate {
    @NotBlank(message = "Category name is required")
    private String name;
}
