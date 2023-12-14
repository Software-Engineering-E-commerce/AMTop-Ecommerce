package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerAddress;
import com.example.BackEnd.Model.CustomerAddressPK;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, CustomerAddressPK> {
    @Transactional
    void deleteByCustomer(Customer customer);

    // Method to return all the CustomerAddress tuples for a given customer
    List<CustomerAddress> findAllByCustomer_Id(Long customerId);

}
