package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.DTO.CategoryResponse;
import com.example.BackEnd.Services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productDetails")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class ViewCategoryController {
    private final CategoryService categoryService;

    //request for getting the category with the specific name.
    @GetMapping("/viewCategory")
    public ResponseEntity<CategoryResponse> viewCategory(HttpServletRequest request, @RequestParam("categoryName") String categoryName) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return ResponseEntity.ok(categoryService.getCategory(categoryName, token));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
