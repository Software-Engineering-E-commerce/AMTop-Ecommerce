package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.HomeInfo;
import com.example.BackEnd.DTO.HomeProductDTO;
import com.example.BackEnd.DTO.HomeProductsDTO;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.WishListRepository;
import com.example.BackEnd.Services.SortServices.SortProducts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeService {
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final WishListRepository wishListRepository;
    private final JwtService jwtService;
    private final SortProducts sortProducts;

    //to search for the username in customers relation otherwise return null indicating not found
    @Transactional
    public Customer getCustomer(String token) {
        String username = jwtService.extractUsername(token);
        return customerRepository.findByEmail(username).orElse(null);
    }


    //to search for the username in admin relation otherwise return null indicating not found
    @Transactional
    public Admin getAdmin(String token) {
        String username = jwtService.extractUsername(token);
        return adminRepository.findByEmail(username).orElse(null);
    }

    //retrieve category list from database
    @Transactional
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    //create the information data transfer object.
    @Transactional
    public HomeInfo getHomeInfo(String token) {
        List<Category> cats = getCategories();
        Customer customer = getCustomer(token);
        Admin admin = getAdmin(token);
        if (customer != null) {
            //customer
            return new HomeInfo(customer.getFirstName(), customer.getLastName(), false, cats);
        } else if (admin != null) {
            //admin
            return new HomeInfo(admin.getFirstName(), admin.getLastName(), true, cats);
        } else {
            //not found
            System.out.println("Not found");
            return null;
        }
    }


    @Transactional
    public HomeProductsDTO getHomeRecommendations(String token) {
        boolean sortAscending = false;
        Long userID;
        Customer customer = getCustomer(token);
        Admin admin = getAdmin(token);
        if (customer != null) {
            // Customer
            userID = customer.getId();
        } else if (admin != null) {
            // Admin
            userID = admin.getId();
        } else {
            // Not found
            return null;
        }

        // Fetch latest, most popular, and most sold products
        List<Product> latestProductsList = ((List<Product>) sortProducts.sort("postedDate", sortAscending)).stream().limit(12).collect(Collectors.toList());
        List<Product> mostPopularProductsList = ((List<Product>) sortProducts.sort("productSoldCount", sortAscending)).stream().limit(12).collect(Collectors.toList());
        List<Product> mostSoldProductsList = ((List<Product>) sortProducts.sort("discountPercentage", sortAscending)).stream().limit(12).collect(Collectors.toList());

        // Convert them to the HomeProductDTO type
        List<HomeProductDTO> latestProducts = mapProductsListToDTOs(latestProductsList, userID );
        List<HomeProductDTO> mostPopularProducts = mapProductsListToDTOs(mostPopularProductsList, userID);
        List<HomeProductDTO> mostSoldProducts = mapProductsListToDTOs(mostSoldProductsList,userID );

        // And then convert the DTOs to a HomeProductsDTO type
        return HomeProductsDTO.builder()
                .latestProducts(latestProducts)
                .mostPopularProducts(mostPopularProducts)
                .mostSoldProducts(mostSoldProducts)
                .build();
    }

    private List<HomeProductDTO> mapProductsListToDTOs(List<Product> products, Long customerId) {
        return products.stream()
                .map(product -> mapToDTO(product, customerId))
                .collect(Collectors.toList());
    }

    public HomeProductDTO mapToDTO(Product product, Long customerId) {
        return HomeProductDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .imageLink(product.getImageLink())
                .discountPercentage(product.getDiscountPercentage())
                .reviews(product.getReviews().stream().map(Review::getRating).collect(Collectors.toList()))
                .inWishlist(isInWishlist(customerId, product))
                .description(product.getDescription())
                .productCountAvailable(product.getProductCountAvailable())
                .categoryName(product.getCategory().getCategoryName())
                .brand(product.getBrand())
                .build();
    }

    @Transactional
    public boolean isInWishlist(Long customerId, Product product) {
        Optional<WishList> wishList = wishListRepository.findByCustomer_IdAndProduct_Id(customerId, product.getId());
        return wishList.isPresent();
    }
}
