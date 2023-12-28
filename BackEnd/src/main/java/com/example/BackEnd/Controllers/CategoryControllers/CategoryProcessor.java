package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.DTO.CategoryDTO;
import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class CategoryProcessor {
    private final Permissions permissions;

    private final CategoryService categoryService;

    public CategoryProcessor(Permissions permissions, CategoryService categoryService){
        this.permissions = permissions;
        this.categoryService = categoryService;
    }

    public ResponseEntity<String> addCategory(String jsonString, MultipartFile image, String authorizationHeader){
        if (permissions.checkToken(authorizationHeader)) {
            String token = authorizationHeader.substring(7);
            try {
                // verify admin
                if (!permissions.checkAdmin(token)) { // unauthorized user
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
                }

                // extract categoryDTO from jsonString
                ObjectMapper map = new ObjectMapper();
                map.registerModule(new JavaTimeModule());
                CategoryDTO newCategoryDTO = map.readValue(jsonString, CategoryDTO.class);

                // add category
                categoryService.addCategory(newCategoryDTO, image);
                return ResponseEntity.status(HttpStatus.OK).body(categoryService.getSuccessMessage(true));
            } catch (Exception e) { // can not add category
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    public ResponseEntity<String> editCategory(String jsonString, MultipartFile image, String authorizationHeader, String originalName){
        if (permissions.checkToken(authorizationHeader)) {
            String token = authorizationHeader.substring(7);
            try {
                // verify admin
                if (!permissions.checkAdmin(token)) { // unauthorized user
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
                }

                // extract categoryDTO from jsonString
                ObjectMapper map = new ObjectMapper();
                map.registerModule(new JavaTimeModule());
                CategoryDTO newCategoryDTO = map.readValue(jsonString, CategoryDTO.class);

                // edit category
                categoryService.editCategory(newCategoryDTO,image, originalName);
                return ResponseEntity.status(HttpStatus.OK).body(categoryService.getSuccessMessage(false));
            } catch (Exception e) { // can not edit category
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
