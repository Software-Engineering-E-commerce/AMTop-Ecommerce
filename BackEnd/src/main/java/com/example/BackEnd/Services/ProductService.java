package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.ProductDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageService ImageService;
    private final CategoryRepository categoryRepository;

    public void addProduct(ProductDTO productDTO, MultipartFile image) throws IOException, IllegalStateException {
        Product product = new Product();
        Optional<Category> category = categoryRepository.findById(productDTO.getCategory());
        if (category.isEmpty()) {
            throw new NoSuchElementException("Category does not exist");
        }
        try {
            product.setProductName(productDTO.getProductName());
            product.setPrice(productDTO.getPrice());
            product.setPostedDate(productDTO.getPostedDate());
            product.setDescription(productDTO.getDescription());
            product.setProductCountAvailable(productDTO.getProductCountAvailable());
            product.setBrand(productDTO.getBrand());
            product.setCategory(category.get());
            productRepository.save(product);

            Long id = product.getId();
            String imageLink = ImageService.saveImage(image, id);
            product.setImageLink(imageLink);
            productRepository.save(product);
        } catch (IOException e) {
            throw new IOException("Could not save image");
        }
    }

    // TODO: after implementing the add product method, implement this method
    public void updateProduct() {

    }

    // TODO: after implementing the update product method, implement this method
    public void deleteProduct() {

    }
}
