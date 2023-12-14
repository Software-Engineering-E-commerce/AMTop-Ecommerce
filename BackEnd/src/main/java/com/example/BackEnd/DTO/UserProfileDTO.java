package com.example.BackEnd.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String contactPhone;    // will be null in case of customer
    private List<String> addresses;
    private boolean isCustomer;

    public void setIsCustomer(boolean b) {
        this.isCustomer = b;
    }

    public boolean getIsCustomer() {
        return isCustomer;
    }
}
