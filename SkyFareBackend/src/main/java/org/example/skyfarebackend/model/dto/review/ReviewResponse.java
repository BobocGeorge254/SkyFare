package org.example.skyfarebackend.model.dto.review;

import lombok.Builder;
import lombok.Data;
import org.example.skyfarebackend.model.dto.book.BookResponse;
import org.example.skyfarebackend.model.dto.userprofile.UserProfileResponse;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private int rating;
    private String comment;
    private UserProfileResponse userProfile;
    private BookResponse book;
}
