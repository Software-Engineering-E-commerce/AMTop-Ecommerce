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
public class HomeProductsDTO {
    private List<HomeProductDTO> latestProducts;
    private List<HomeProductDTO> mostPopularProducts;
    private List<HomeProductDTO> mostSoldProducts;
}
