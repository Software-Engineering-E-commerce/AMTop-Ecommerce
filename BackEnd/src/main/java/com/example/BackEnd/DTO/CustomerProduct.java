package com.example.BackEnd.DTO;

import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProduct {
    private Long id;
    private String productName;
    private float price;
    private LocalDateTime postedDate;
    private String description;
    private int productCountAvailable;
    private int productSoldCount;
    private String brand;
    private String imageLink;
    private float discountPercentage;
    private Category category;
    private List<Review> reviews;
    private boolean inWishlist;
    private boolean inCart;
}
