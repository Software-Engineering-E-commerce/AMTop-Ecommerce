package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/editCategory")
@CrossOrigin(origins = "http://localhost:3000/")
public class EditCategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Permissions permissions;

    @PostMapping
    public ResponseEntity<String> editCategory(@RequestParam(value = "categoryDTO") String jsonString,
                                               @RequestParam("image") MultipartFile image,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        CategoryProcessor categoryProcessor = new CategoryProcessor(permissions, categoryService);
        return categoryProcessor.editCategory(jsonString, image, authorizationHeader);
    }
}
