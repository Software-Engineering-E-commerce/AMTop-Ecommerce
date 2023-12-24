package com.example.BackEnd.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    private LocalDateTime postedDate;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false)
    private int productCountAvailable;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int productSoldCount;
    @Column(nullable = false)
    private String brand;
    @Column
    private String imageLink;
    @Column(nullable = false, columnDefinition = "float default 0.0")
    private float discountPercentage;
    @ManyToOne
    @JoinColumn(name = "category_name", nullable = false)
    //json ignore
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();
}
