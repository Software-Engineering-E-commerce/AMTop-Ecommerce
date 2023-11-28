package com.example.BackEnd.Service;


import com.example.BackEnd.DataObject.CustomerDataObject;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository CustomerRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo) {
        this.CustomerRepo = customerRepo;
    }

    @Override
    public String save(CustomerDataObject CDto) {
        Customer customer = new Customer(CDto.getEmail(),CDto.getPassword(),CDto.getIsGmail()
        ,CDto.getIsVerified(),CDto.getFirstName(),CDto.getLastName(),CDto.getPhoneNumber(),CDto.getAddresses());

        try {
            Optional<Customer> customerCheck = CustomerRepo.findByEmail(customer.getEmail());
            if (customerCheck.isPresent()) {
                return "Already Exists!!";
            } else {
                CustomerRepo.save(customer);
                //verify
                return "Success";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}
