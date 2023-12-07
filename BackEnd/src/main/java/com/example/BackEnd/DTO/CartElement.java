package com.example.BackEnd.DTO;

import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartElement {
    private Long id;
    private String productName;
    private float price;
    private String description;
    private String imageLink;
    private int quantity;
    private String token;
}
