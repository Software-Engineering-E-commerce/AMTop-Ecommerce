package com.example.BackEnd.DataObject;

import com.example.BackEnd.Model.CustomerAddress;
import com.example.BackEnd.Model.User;


import java.util.List;

public class CustomerDataObject extends User {


    private List<CustomerAddress> addresses;

    public CustomerDataObject(List<CustomerAddress> addresses) {
        this.addresses = addresses;
    }

    public List<CustomerAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<CustomerAddress> addresses) {
        this.addresses = addresses;
    }
}
