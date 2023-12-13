package com.example.BackEnd.Services.ProductServices;

import com.example.BackEnd.DTO.ProductDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;

public abstract class AbstractProductService implements IProductService {

    protected void setProduct(ProductDTO productDTO, Product product, Category category) {
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setPostedDate(productDTO.getPostedDate());
        product.setDescription(productDTO.getDescription());
        product.setProductCountAvailable(productDTO.getProductCountAvailable());
        product.setBrand(productDTO.getBrand());
        product.setDiscountPercentage(productDTO.getDiscountPercentage());
        product.setCategory(category);
    }
}
