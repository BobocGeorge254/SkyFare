package org.example.skyfarebackend.model.dto.category;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryUpdate {
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String name;
}
