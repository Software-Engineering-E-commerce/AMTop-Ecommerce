package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByGmail(String gmail);
}
