package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.ProductResponse;
import com.example.BackEnd.DTO.ReviewResponse;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.Review;
import com.example.BackEnd.Model.WishList;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDetailsService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    private boolean isAdmin(String token){
        String email = jwtService.extractUsername(token);
        return adminRepository.findByEmail(email).isPresent();
    }

    private boolean checkInWishlist(String token, Product product){
        String email = jwtService.extractUsername(token);
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            return customer.getWishList().contains(new WishList(customer, product));
        }
        return false;
    }

    //function to get the product by its id and return its DTO if found else return null
    public ProductResponse getProduct(Long productID, String token){
        try {
            Optional<Product> optionalProduct = productRepository.findById(productID);
            if(optionalProduct.isPresent()){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                Product product = optionalProduct.get();
                List<ReviewResponse> reviewResponses = new ArrayList<>();
                long reviewId = 1;
                for(Review review : product.getReviews()){
                    LocalDateTime date = review.getDate();
                    String formattedDate = date.format(formatter);
                    ReviewResponse reviewResponse = new ReviewResponse(reviewId++, review.getCustomer().getEmail(), review.getRating(), review.getComment(), formattedDate);
                    reviewResponses.add(reviewResponse);
                }
                String postDate = product.getPostedDate().format(formatter);
                return ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getProductName())
                        .imageUrl(product.getImageLink())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .postedDate(postDate)
                        .productCountAvailable(product.getProductCountAvailable())
                        .productCountSold(product.getProductSoldCount())
                        .brand(product.getBrand())
                        .discountPercentage(product.getDiscountPercentage())
                        .categoryName(product.getCategory().getCategoryName())
                        .categoryUrl(product.getCategory().getImageLink())
                        .reviews(reviewResponses)
                        .isAdmin(isAdmin(token))
                        .isInWishlist(checkInWishlist(token, product))
                        .build();
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
