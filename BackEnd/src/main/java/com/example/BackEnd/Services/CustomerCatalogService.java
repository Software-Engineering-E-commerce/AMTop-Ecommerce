package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.CustomerProduct;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.WishList;
import com.example.BackEnd.Repositories.CustomerCartRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.example.BackEnd.Repositories.WishListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerCatalogService {
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    private final CustomerCartRepository customerCartRepository;

    @Transactional
    public List<CustomerProduct> getProducts (Long customerId) {
        List<CustomerProduct> returnedProducts = new ArrayList<>();
        List<Product> originalProducts;
        try {
            originalProducts = productRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        for (Product product: originalProducts) {
            Optional<WishList> wishList;
            try {
                wishList = wishListRepository.findByCustomer_IdAndProduct_Id(customerId, product.getId());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ArrayList<>();
            }
            Optional<CustomerCart> customerCart;
            try {
                customerCart = customerCartRepository.findByCustomer_IdAndProduct_Id(customerId, product.getId());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new ArrayList<>();
            }
            CustomerProduct customerProduct = CustomerProduct.builder()
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
                    .inWishlist(wishList.isPresent())
                    .inCart(customerCart.isPresent())
                    .build();
            returnedProducts.add(customerProduct);
        }
        return returnedProducts;
    }
}
