package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserVerification {

    private final JwtService jwtService;
    private final CustomerRepository CustomerRepository;

    public void verifyUser(String token) {
        String username = jwtService.extractUsername(token);
        Optional<Customer> customer = CustomerRepository.findByEmail(username);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        Customer user = customer.get();
        if (!user.getIsVerified()) {
            user.setIsVerified(true);
            CustomerRepository.save(user);
        }
        else {
            throw new IllegalStateException("User already verified");
        }
    }
}
