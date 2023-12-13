package com.example.BackEnd.Controllers.ProductControllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.ProductServices.UpdateProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/updateProduct")
@CrossOrigin(origins = "http://localhost:3000/")
public class UpdateProductController {

    @Autowired
    private UpdateProductService updateProductService;
    @Autowired
    private Permissions permissions;

    @PostMapping
    public ResponseEntity<String> updateProduct(@RequestParam(value = "productDTO") String jsonString,
                                                @RequestParam("image") MultipartFile image,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        ProductProcessor productProcessor = new ProductProcessor(permissions, updateProductService);
        return productProcessor.processProduct(jsonString, image, authorizationHeader);
    }
}
