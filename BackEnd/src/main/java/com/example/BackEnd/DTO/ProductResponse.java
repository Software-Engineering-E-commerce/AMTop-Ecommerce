package com.example.BackEnd.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private float price;
    private String postedDate;
    private int productCountAvailable;
    private int productCountSold;
    private String brand;
    private float discountPercentage;
    private String categoryName;
    private String categoryUrl;
    private List<ReviewResponse> reviews;
    private boolean isAdmin;
    private boolean isInWishlist;
}
