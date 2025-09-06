package org.example.skyfarebackend.model.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private int rating;
    private String comment;
    private String firstName;
    private String lastName;
}
