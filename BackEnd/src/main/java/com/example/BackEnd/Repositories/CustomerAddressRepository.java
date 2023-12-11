package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.CustomerAddress;
import com.example.BackEnd.Model.CustomerAddressPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, CustomerAddressPK> {
    // Method to return all the CustomerAddress tuples for a given customer
    List<CustomerAddress> findAllByCustomer_Id(Long customerId);

}
