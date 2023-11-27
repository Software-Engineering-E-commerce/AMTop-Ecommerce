package com.example.BackEnd.Model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isGmail;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isVerified;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String phoneNumber;
}
