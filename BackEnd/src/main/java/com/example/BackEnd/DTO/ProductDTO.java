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
public class ProductDTO {
    private String productName;
    private float price;
    private LocalDateTime postedDate;
    private String description;
    private int productCountAvailable;
    private String brand;
    private String category;
}
