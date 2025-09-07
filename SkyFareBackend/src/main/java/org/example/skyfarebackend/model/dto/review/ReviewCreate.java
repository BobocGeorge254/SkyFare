package org.example.skyfarebackend.model.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreate {
    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "User profile ID is required")
    private Long userProfileId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Comment is required")
    private String comment;
}
