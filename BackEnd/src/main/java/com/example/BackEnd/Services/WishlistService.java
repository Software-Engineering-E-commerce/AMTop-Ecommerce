package com.example.BackEnd.Services;
import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.WishlistElement;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.WishList;
import com.example.BackEnd.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final CustomerRepository customerRepository;
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final JwtService jwtService;

    // Get the customer entity using his token
    @Transactional
    public Customer getCustomer(String token){
        String username = jwtService.extractUsername(token);
        return customerRepository.findByEmail(username).orElse(null);
    }

    // Getting the wishlist element DTO that's equivalent to the Wishlist entity
    public WishlistElement convertToWishlistElement(WishList wishList, String token){
        return WishlistElement.builder()
                .id(wishList.getProduct().getId())
                .description(wishList.getProduct().getDescription())
                .imageLink(wishList.getProduct().getImageLink())
                .discountPercentage(wishList.getProduct().getDiscountPercentage())
                .productName(wishList.getProduct().getProductName())
                .token(token)
                .price(wishList.getProduct().getPrice())
                .build();
    }


    // Adding a product in the customer's wishlist
    @Transactional
    public void addToWishlist(String token, Long productID) throws IllegalAccessException {
        Customer customer = getCustomer(token);
        boolean isInWishlist = wishListRepository.existsByCustomer_IdAndProduct_Id(customer.getId(), productID);
        if(!isInWishlist){
            Optional<Product> product = productRepository.findProductById(productID);
            if (product.isEmpty()){
                // Here means that the product doesn't exist
                throw new IllegalAccessException("Product not exist");
            } else{
                // Here all set let's add the product to the wishlist
                WishList wishList  = new WishList(customer, product.get());
                wishListRepository.save(wishList);
            }
        } else{
            // Here means that the product's already in the wishlist
            throw new IllegalStateException("Product's already in the wishlist !");
        }
    }

    // Removing a product from the customer's wishlist
    @Transactional
    public void deleteFromWishlist(String token, Long productID){
        Customer customer = getCustomer(token);
        if(wishListRepository.existsByCustomer_IdAndProduct_Id(customer.getId(),productID)){
            // Then we need to delete the entry in the table of customer and productId
            wishListRepository.deleteByCustomerAndProduct_Id(customer, productID);
        }else{
            // Product not in the wishlist
            throw new IllegalStateException("Product is not in the wishlist");
        }
    }

    // Removing the product from the customer's wishlist and adding it to his cart
    @Transactional
    public void addToCart(String token, Long productId) throws IllegalAccessException {
        cartService.addToCart(token,productId);
        deleteFromWishlist(token, productId);
    }

    // Getting all the wishlist elements
    @Transactional
    public List<WishlistElement> getWishlistElements(String token){
        Customer customer = getCustomer(token);
        List<WishlistElement> wishlistElements = new ArrayList<>();
        List<WishList> wishLists = wishListRepository.findByCustomer_Id(customer.getId());
        for(WishList wishList: wishLists){
            WishlistElement wishlistElement = convertToWishlistElement(wishList,token);
            wishlistElements.add(wishlistElement);
        }
        return wishlistElements;
    }

}
