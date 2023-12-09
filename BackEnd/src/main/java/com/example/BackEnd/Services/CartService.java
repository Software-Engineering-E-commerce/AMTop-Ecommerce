package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.CartElement;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CustomerCartRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CustomerCartRepository customerCartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;

    Customer getCustomer(String token){
        String username = jwtService.extractUsername(token);
        return customerRepository.findByEmail(username).orElse(null);
    }

    public CartElement convertToCartElement(CustomerCart customerCart, String token) {
        // Convert CustomerCart entity to CartElement DTO
        return CartElement.builder()
                .id(customerCart.getProduct().getId())
                .productName(customerCart.getProduct().getProductName())
                .discountPercentage(customerCart.getProduct().getDiscountPercentage())
                .price(customerCart.getProduct().getPrice())
                .description(customerCart.getProduct().getDescription())
                .imageLink(customerCart.getProduct().getImageLink())
                .quantity(customerCart.getQuantity())
                .token(token)
                .build();
    }
    public void addToCart(String token, Long productID) throws IllegalAccessException {
        Customer customer = getCustomer(token);
        //now let's add the product to the cart
        if(customerCartRepository.existsByCustomerAndProduct_Id(customer,productID)){
            //Customer wants to add a product that alrady exists to his cart
            throw new IllegalStateException("Product's already in the cart");
        }else{
            //here our customer exists and the product is not in his cart.
            Optional<Product> product = productRepository.findProductById(productID);
            if(product.isEmpty()){
                throw new IllegalAccessException("Product not exist");
            }else if(product.get().getProductCountAvailable() == 0){
                throw new IllegalAccessException("Product out of stock");
            }else{
                //here all is set, then we add it to his cart
                CustomerCart customerCart = new CustomerCart(customer,product.get(),1);
                customerCartRepository.save(customerCart);
            }
        }

    }

    public void setQuantity(String token, Long productID, int quantity){
        Customer customer = getCustomer(token); //the customer is verified to exist when adding to the cart
        if(customerCartRepository.existsByCustomerAndProduct_Id(customer,productID)){
            //the product is in the cart
            Optional<Product> product = productRepository.findProductById(productID); //there's at least one item of that product (tested in addToCart)
            if(product.get().getProductCountAvailable() < quantity){
                //desired exceeds the available
                throw new IllegalStateException("Sorry, the requested quantity exceeds the available count for this product");
            }else{
                //here we can set the new quantity
                Optional<CustomerCart> customerCart = customerCartRepository.findByCustomerAndProduct_Id(customer,productID);
                customerCart.ifPresent(cartEntry -> {
                    //update the quantity
                    cartEntry.setQuantity(quantity);
                    //set the updated entry to the DB
                    customerCartRepository.save(cartEntry);
                });
            }
        }else{
            //product not in the cart
            throw new IllegalStateException("Product is not in the cart");
        }
    }

    @Transactional
    public void deleteFromCart(String token, Long productID){
        Customer customer = getCustomer(token);
        if(customerCartRepository.existsByCustomerAndProduct_Id(customer,productID)){
            //then we need to delete the entry in the table of customer and productId
            customerCartRepository.deleteByCustomerAndProduct_Id(customer, productID);
        }else{
            //product not in the cart
            throw new IllegalStateException("Product is not in the cart");
        }
    }

    public List<CartElement> getCartElements(String token){
        Customer customer = getCustomer(token);
        List<CartElement> cartElements = new ArrayList<>();
        List<CustomerCart> customerCarts =  customerCartRepository.findByCustomer_Id(customer.getId());
        for(CustomerCart customerCart : customerCarts){
            CartElement cartElement = convertToCartElement(customerCart, token);
            cartElements.add(cartElement);
        }
        return cartElements;
    }

}
