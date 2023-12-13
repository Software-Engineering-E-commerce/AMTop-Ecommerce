package com.example.BackEnd.Controllers.ProductControllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.ProductServices.AddProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/addProduct")
@CrossOrigin(origins = "http://localhost:3000/")
public class AddProductController {

    @Autowired
    private AddProductService addProductService;
    @Autowired
    private Permissions permissions;

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestParam(value = "productDTO") String jsonString,
                                             @RequestParam("image") MultipartFile image,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        ProductProcessor productProcessor = new ProductProcessor(permissions, addProductService);
        return productProcessor.processProduct(jsonString, image, authorizationHeader);
    }
}
