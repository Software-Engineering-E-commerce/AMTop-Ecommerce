package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CustomerCartRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public void addToCart(String token, Long productID) throws IllegalAccessException {
        Customer customer = getCustomer(token);
        if(customer != null){
            //then our customer exist, and he must exist, but we're checking anyway
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

        }else{
            throw new UsernameNotFoundException("Customer is not found");
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


}
