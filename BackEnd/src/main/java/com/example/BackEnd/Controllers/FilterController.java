package com.example.BackEnd.Controllers;

import com.example.BackEnd.Services.FilterService.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filter")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class FilterController<T extends Comparable<T>> {

    private final FilterService<T> filterService;

    @PostMapping("/{entity}")
    public List<T> filter(@PathVariable String entity, @RequestBody Object criteria) {
        System.out.println("entity: " + entity + " criteria: " + criteria);
        return filterService.filter(entity, criteria);
    }
}
