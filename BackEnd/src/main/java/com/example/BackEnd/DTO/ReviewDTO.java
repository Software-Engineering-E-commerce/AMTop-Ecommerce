package com.example.BackEnd.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private String customerName;
    private float rating;
    private String comment;
    private LocalDateTime date;
    private Long productId;
    private boolean hasReviewed;
    public ReviewDTO(String customerName, float rating, String comment, LocalDateTime date, boolean hasReviewed) {
        this.customerName = customerName;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.hasReviewed = hasReviewed;
    }
}
