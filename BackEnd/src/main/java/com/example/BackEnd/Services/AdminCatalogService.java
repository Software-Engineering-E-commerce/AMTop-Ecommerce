package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.AdminProduct;
import com.example.BackEnd.DTO.CustomerProduct;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCatalogService {
    private final ProductRepository productRepository;

    @Transactional
    public List<CustomerProduct> getAdminProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToAdminProduct)
                .collect(Collectors.toList());
    }

    public CustomerProduct convertToAdminProduct(Product product) {
        return CustomerProduct.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .postedDate(product.getPostedDate())
                .description(product.getDescription())
                .productCountAvailable(product.getProductCountAvailable())
                .productSoldCount(product.getProductSoldCount())
                .brand(product.getBrand())
                .imageLink(product.getImageLink())
                .discountPercentage(product.getDiscountPercentage())
                .category(product.getCategory())
                .reviews(product.getReviews())
                .inWishlist(false)
                .inCart(false)
                .build();
    }

}
