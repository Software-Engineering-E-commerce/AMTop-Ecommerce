package com.example.BackEnd.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterProductDto {
    private String productName;
    private float fromPrice;
    private float toPrice;
    private String description;
    private boolean available;
    private String brand;
    private float toDiscountPercentage;
    private float fromDiscountPercentage;
    private String category;
}
