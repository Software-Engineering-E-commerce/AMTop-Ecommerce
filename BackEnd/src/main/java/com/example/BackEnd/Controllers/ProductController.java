package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.ProductDTO;
import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:3000/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Permissions permissions;

    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestParam(value = "productDTO") String jsonString,
                                             @RequestParam("image") MultipartFile image,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (permissions.checkToken(authorizationHeader)) {
            String token = authorizationHeader.substring(7);
            try {
                // verify admin
                if (!permissions.checkAdmin(token)) { // unauthorized user
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
                }

                // extract productDTO from jsonString
                ObjectMapper map = new ObjectMapper();
                map.registerModule(new JavaTimeModule());
                ProductDTO newProductDTO = map.readValue(jsonString, ProductDTO.class);

                // add product
                productService.addProduct(newProductDTO, image);
                return ResponseEntity.status(HttpStatus.OK).body("Product added successfully");
            } catch (Exception e) { // can not add product
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
