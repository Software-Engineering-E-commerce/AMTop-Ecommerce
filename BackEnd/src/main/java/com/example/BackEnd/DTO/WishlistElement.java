package com.example.BackEnd.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishlistElement {
    private Long id;
    private String productName;
    private float price;
    private String description;
    private String imageLink;
    private String token;
    private float discountPercentage;
}
