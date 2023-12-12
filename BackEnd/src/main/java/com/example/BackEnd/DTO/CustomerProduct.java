package com.example.BackEnd.DTO;

import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Review;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
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
