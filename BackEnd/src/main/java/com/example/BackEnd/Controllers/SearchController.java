package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.CustomerProduct;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.*;
import com.example.BackEnd.Services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final WishListRepository wishListRepository;
    private final CustomerCartRepository customerCartRepository;
    private final JwtService jwtService;

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @GetMapping("/{key}")
    public List<CustomerProduct> search(@RequestHeader("Authorization") String authorizationHeader,
                                        @PathVariable String key) {
        String token = extractToken(authorizationHeader);
        String email = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByEmail(email);
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (customer.isPresent()) {
            Long customerId = customer.get().getId();
            List<Product> originalProducts = searchService.search(key);
            return getCustomerProducts(originalProducts, customerId);
        } else if (admin.isPresent()) {
            Long adminId = admin.get().getId();
            List<Product> originalProducts = searchService.search(key);
            return getCustomerProducts(originalProducts, adminId);
        }
        else
            return new ArrayList<>();
    }

    private List<CustomerProduct> getCustomerProducts(List<Product> originalProducts, Long customerId) {
        List<CustomerProduct> returnedProducts = new ArrayList<>();
        for (Product product : originalProducts) {
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
