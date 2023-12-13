package com.example.BackEnd.Controllers.ProductControllers;

import com.example.BackEnd.DTO.ProductDTO;
import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.ProductServices.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

public class ProductProcessor {

    private final Permissions permissions;
    private final IProductService productService;

    public ProductProcessor(Permissions permissions, IProductService productService) {
        this.permissions = permissions;
        this.productService = productService;
    }


    public ResponseEntity<String> processProduct(String jsonString, MultipartFile image, String authorizationHeader) {
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
                productService.processProduct(newProductDTO, image);
                return ResponseEntity.status(HttpStatus.OK).body(productService.getSuccessMessage());
            } catch (Exception e) { // can not add product
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
