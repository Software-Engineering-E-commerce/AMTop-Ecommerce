package com.example.BackEnd.Services.ProductServices;

import com.example.BackEnd.DTO.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

public interface IProductService {
    void processProduct(ProductDTO productDTO, MultipartFile image) throws IOException, NoSuchElementException;
    String getSuccessMessage();
}
