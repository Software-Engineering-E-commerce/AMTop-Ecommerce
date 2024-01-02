package com.example.BackEnd.DTO;
import com.example.BackEnd.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeProductDTO {
    private Long id;
    private String productName;
    private float price;
    private String imageLink;
    private float discountPercentage;
    private List<Float> reviews;
    private boolean inWishlist;
    private String description;
    private int productCountAvailable;
    private String categoryName;
    private String brand;
}
