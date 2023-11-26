package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.CustomerAddress;
import com.example.BackEnd.Model.CustomerAddressPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, CustomerAddressPK> {
}
