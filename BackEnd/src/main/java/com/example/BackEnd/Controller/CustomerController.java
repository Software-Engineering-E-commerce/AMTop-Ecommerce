package com.example.BackEnd.Controller;

import com.example.BackEnd.DataObject.CustomerDataObject;
import com.example.BackEnd.Service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
public class CustomerController {

    private CustomerService CServe;

    public CustomerController(CustomerService CServe) {
        this.CServe = CServe;
    }

    @PostMapping
    public String CustomerReg(@RequestBody CustomerDataObject CDto){

        return CServe.save(CDto);
    }
}
