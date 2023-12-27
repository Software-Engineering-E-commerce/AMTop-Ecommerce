package com.example.BackEnd.Controllers;

import com.example.BackEnd.Services.SortServices.SortService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sort")
@CrossOrigin(origins = "http://localhost:3000")
public class SortController<T extends Comparable<T>> {

    private final SortService<T> sortService;

    @GetMapping("/{entity}/{sortBy}/{sortOrder}")
    public List<T> sort(@PathVariable String entity, @PathVariable String sortBy, @PathVariable boolean sortOrder) {
        System.out.println("SortController: sort");
        System.out.println("entity: " + entity);
        System.out.println("sortBy: " + sortBy);
        System.out.println("sortOrder: " + sortOrder);
        return sortService.sort(entity, sortBy, sortOrder);
    }

}
