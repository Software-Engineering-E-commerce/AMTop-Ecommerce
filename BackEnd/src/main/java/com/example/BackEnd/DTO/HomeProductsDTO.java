package com.example.BackEnd.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeProductsDTO {
    private HomeProductDTO latestProducts;
    private HomeProductDTO mostPopularProducts;
    private HomeProductDTO mostSoldProducts;
}
