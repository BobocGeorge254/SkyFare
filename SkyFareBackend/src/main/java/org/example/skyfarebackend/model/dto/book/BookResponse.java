package org.example.skyfarebackend.model.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.skyfarebackend.model.dto.author.AuthorResponse;
import org.example.skyfarebackend.model.dto.category.CategoryResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private AuthorResponse author;
    private CategoryResponse category;
}
