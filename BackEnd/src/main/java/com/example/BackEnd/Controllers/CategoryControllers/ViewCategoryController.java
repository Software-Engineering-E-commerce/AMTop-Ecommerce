package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.DTO.CategoryDTO;
import com.example.BackEnd.Services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categoryDetails")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class ViewCategoryController {
    private final CategoryService categoryService;

    //request for getting the category with the specific name.
    @GetMapping("/viewCategory")
    public ResponseEntity<CategoryDTO> viewCategory(HttpServletRequest request, @RequestParam("categoryName") String categoryName) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(categoryService.getCategory(categoryName));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
