package com.example.BackEnd.Controllers;

import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/{key}")
    public List<Product> search(@PathVariable String key) {
        return searchService.search(key);
    }
}
