package com.example.BackEnd.Service;

import com.example.BackEnd.DataObject.CustomerDataObject;
import com.example.BackEnd.Model.Customer;

public interface CustomerService {
    String save(CustomerDataObject CDto);
}
