package com.example.BackEnd.Model;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore //to break the circular reference
    private List<CustomerAddress> addresses = new ArrayList<>();

    public Customer(String email, String password, Boolean isGmail, Boolean isVerified, String firstName, String lastName) {
        this.setEmail(email);
        this.setPassword(password);
        this.setIsGmail(isGmail);
        this.setIsVerified(isVerified);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }
}