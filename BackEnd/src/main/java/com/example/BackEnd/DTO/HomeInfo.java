package com.example.BackEnd.DTO;

import com.example.BackEnd.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeInfo {
    private String firstName;
    private String lastName;
    private boolean isAdmin;
    private List<Category> categoryList;
}
