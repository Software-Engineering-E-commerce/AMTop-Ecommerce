package com.example.BackEnd.Services;
import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.CartElement;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerCartRepository customerCartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;

    // Get the Customer object given his token
    @Transactional
    public Customer getCustomer(String token){
        String username = jwtService.extractUsername(token);
        return customerRepository.findByEmail(username).orElse(null);
    }

    // Convert CustomerCart entity to CartElement DTO
    @Transactional
    public CartElement convertToCartElement(CustomerCart customerCart, String token) {
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

    // Add a product to the customer's cart given his token and the ID of that product
    @Transactional
    public void addToCart(String token, Long productID) throws IllegalAccessException {
        Customer customer = getCustomer(token);
        // Now let's add the product to the cart
        if(customerCartRepository.existsByCustomerAndProduct_Id(customer,productID)){
            // Customer wants to add a product that already exists to his cart
            throw new IllegalStateException("Product's already in the cart");
        }else{
            // Here our customer exists and the product is not in his cart.
            Optional<Product> product = productRepository.findProductById(productID);
            if(product.isEmpty()){
                throw new IllegalAccessException("Product not exist");
            }else if(product.get().getProductCountAvailable() == 0){
                throw new IllegalAccessException("Product out of stock");
            }else{
                // Here all is set, then we add it to his cart
                CustomerCart customerCart = new CustomerCart(customer,product.get(),1);
                customerCartRepository.save(customerCart);
            }
        }

    }

    // Setting a new quantity of a product in the customer's cart
    @Transactional
    public void setQuantity(String token, Long productID, int quantity){
        Customer customer = getCustomer(token); //the customer is verified to exist when adding to the cart
        if(customerCartRepository.existsByCustomerAndProduct_Id(customer,productID)){
            // The product is in the cart
            Optional<Product> product = productRepository.findProductById(productID); //there's at least one item of that product (tested in addToCart)
            if(product.get().getProductCountAvailable() < quantity){
                // Desired exceeds the available
                throw new IllegalStateException("Sorry, the requested quantity exceeds the available count for this product");
            }else{
                // Here we can set the new quantity
                Optional<CustomerCart> customerCart = customerCartRepository.findByCustomerAndProduct_Id(customer,productID);
                customerCart.ifPresent(cartEntry -> {
                    // Update the quantity
                    cartEntry.setQuantity(quantity);
                    // Set the updated entry to the DB
                    customerCartRepository.save(cartEntry);
                });
            }
        }else{
            //product not in the cart
            throw new IllegalStateException("Product is not in the cart");
        }
    }

    // Delete some product given its ID from the customer's cart
    @Transactional
    public void deleteFromCart(String token, Long productID){
        Customer customer = getCustomer(token);
        if(customerCartRepository.existsByCustomerAndProduct_Id(customer,productID)){
            // Then we need to delete the entry in the table of customer and productId
            customerCartRepository.deleteByCustomerAndProduct_Id(customer, productID);
        }else{
            // Product not in the cart
            throw new IllegalStateException("Product is not in the cart");
        }
    }

    // Get the all the products in the customer's cart as a list of cartElement objects
    @Transactional
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

    // The checkout service function
    @Transactional
    public void checkout(String token){
        // First we need to get our customer object
        Customer customer = getCustomer(token);
        // Then we need to check that he has at least one element in the cart
        List<CustomerCart> customerCarts =  customerCartRepository.findByCustomer_Id(customer.getId());
        if (customerCarts.size() != 0){
            List<CustomerAddress> customerAddresses = customerAddressRepository.findAllByCustomer_Id(customer.getId());
            // Checking that there's at least one address in the list
            if (!customerAddresses.isEmpty()){
                // Here our customer has an address and products in his cart
                String address = customerAddresses.get(0).getAddress();
                for(CustomerCart customerCart: customerCarts){
                    if (customerCart.getQuantity() > customerCart.getProduct().getProductCountAvailable()){
                        // Here we have a product whose quantity is greater than the available
                        throw new IllegalStateException("The available in stock right now " +
                                "for " + customerCart.getProduct().getProductName() + " product is only " + customerCart.getProduct().getProductCountAvailable());
                    }
                }
                // Here means that we're all set for building the order parts
                buildOrder(customerCarts,address);

            } else{
                // Here there's no address provided for this customer, so we throw an exception
                throw new IllegalStateException("No address provided yet !");
            }

        } else{
            // Here the cart is empty !
            throw new IllegalStateException("Can't happen, your cart is empty !");
        }
    }

    @Transactional
    public void buildOrder(List<CustomerCart> customerCarts, String address){
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        float total_cost = (float) 0.0;
        int total_amount = 0;
        for(CustomerCart customerCart: customerCarts){
            OrderItem orderItem = new OrderItem();
            Product product = customerCart.getProduct();
            total_cost += ((100.0 - product.getDiscountPercentage())/100.0)
                    * product.getPrice() * customerCart.getQuantity();
            total_amount += customerCart.getQuantity();

            // Here we need to decrement the availability count of this product by the quantity
            // And as well increment the amount sold of this product by the quantity
            product.setProductSoldCount(product.getProductSoldCount() + customerCart.getQuantity());
            product.setProductCountAvailable(product.getProductCountAvailable() - customerCart.getQuantity());

            // Let's build OrderItem one at a time
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setOriginalCost(product.getPrice());
            orderItem.setQuantity(customerCart.getQuantity());
            orderItems.add(orderItem);
        }
        // Let's build our order
        order.setStartDate(LocalDateTime.now());
        order.setDeliveryDate(LocalDateTime.now().plusDays(7)); // Default number of days for delivery
        order.setAddress(address);
        order.setTotalAmount(total_amount);
        order.setTotalCost(total_cost);
        order.setCustomer(customerCarts.get(0).getCustomer());
        order.setOrderItems(orderItems);
        order.setStatus("Pending");
        // And now we save the order we just built
        orderRepository.save(order);
        // Then we delete all the products in the cart of this customer
        customerCartRepository.deleteByCustomer(customerCarts.get(0).getCustomer());
    }
}