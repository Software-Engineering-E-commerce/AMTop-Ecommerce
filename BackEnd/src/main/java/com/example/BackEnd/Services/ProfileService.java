package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.UserProfileDTO;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerAddressRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    @Transactional
    public UserProfileDTO retrieveData(String email) {
        // First, try to find the user in the CustomerRepository
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            Customer user = optionalCustomer.get();
            return mapToCustomerProfileDTO(user);
        }
        // If not found, try to find the user in the AdminRepository
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            Admin user = optionalAdmin.get();
            return mapToAdminProfileDTO(user);
        }
        // If no user is found
        return null;
    }

    public String updateData(UserProfileDTO userProfileDTO, String email){
        try {
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
            if (optionalCustomer.isPresent()) {
                Customer user = optionalCustomer.get();
                // Update basic info
                user.setFirstName(userProfileDTO.getFirstName());
                user.setLastName(userProfileDTO.getLastName());
                user.setPhoneNumber(userProfileDTO.getPhoneNumber());

                // Remove current addresses of this customer
                customerAddressRepository.deleteByCustomer(user);

                // Add the new addresses
                List<CustomerAddress> customerAddresses = new ArrayList<>();
                for(String address: userProfileDTO.getAddresses()){
                    CustomerAddressPK customerAddressPK = new CustomerAddressPK(user, address);
                    if(!customerAddressRepository.findById(customerAddressPK).isPresent()){
                        customerAddresses.add(new CustomerAddress(user, address));
                    }
                }
                user.setAddresses(customerAddresses);
                customerRepository.save(user);

                return "Updated Successfully";
            }

            // ... handle Admin case ...
            Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
            if(optionalAdmin.isPresent()) {
                Admin user = optionalAdmin.get();
                user.setFirstName(userProfileDTO.getFirstName());
                user.setLastName(userProfileDTO.getLastName());
                user.setPhoneNumber(userProfileDTO.getPhoneNumber());
                if(userProfileDTO.getAddresses().size() > 0){
                    user.setAddress(userProfileDTO.getAddresses().get(0));
                }else{
                    user.setAddress(null);
                }
                user.setContactPhone(userProfileDTO.getContactPhone());
                adminRepository.save(user);

                return "Updated Successfully";
            }
            
            return "User not found with email: " + email;
        } catch (Exception e) {
            return "An error occurred\nPlease try again";
        }
    }

    private UserProfileDTO mapToCustomerProfileDTO(Customer user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        List<String> addresses = new ArrayList<>();
        for(CustomerAddress address: user.getAddresses()){
            addresses.add(address.getAddress());
        }
        dto.setAddresses(addresses);
        dto.setIsCustomer(true);
        return dto;
    }
    private UserProfileDTO mapToAdminProfileDTO(Admin user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setContactPhone(user.getContactPhone());
        String adminAddress = user.getAddress();
        List<String> addresses = new ArrayList<>();
        addresses.add(adminAddress);
        dto.setAddresses(addresses);
        dto.setIsCustomer(false);
        return dto;
    }

}
