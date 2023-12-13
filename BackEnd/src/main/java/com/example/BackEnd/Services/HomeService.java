package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.HomeInfo;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeService {
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final JwtService jwtService;

    //to search for the username in customers relation otherwise return null indicating not found
    @Transactional
    public Customer getCustomer(String token){
        String username = jwtService.extractUsername(token);
        return customerRepository.findByEmail(username).orElse(null);
    }

    //to search for the username in admin relation otherwise return null indicating not found
    @Transactional
    public Admin getAdmin(String token){
        String username = jwtService.extractUsername(token);
        return adminRepository.findByEmail(username).orElse(null);
    }

    //retrieve category list from database
    @Transactional
    public List<Category> getCategories(String token){
        return categoryRepository.findAll();
    }

    //create the information data transfer object.
    @Transactional
    public HomeInfo getHomeInfo(String token){
        List<Category> cats = getCategories(token);
        Customer customer=getCustomer(token);
        Admin admin = getAdmin(token);
        if(customer!=null){
            //customer
            return new HomeInfo(customer.getFirstName(),customer.getLastName(),false,cats);
        }
        else if(admin!=null){
            //admin
            return new HomeInfo(admin.getFirstName(),admin.getLastName(),true,cats);
        }
        else {
            //not found
            System.out.println("Not found");
            return null;
        }
    }
}
