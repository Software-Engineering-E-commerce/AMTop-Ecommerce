package com.example.BackEnd.Services.ProductServices;

import com.example.BackEnd.DTO.ProductDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.example.BackEnd.Services.ImageService;
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
public class UpdateProductService extends AbstractProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;

    @Override
    public void processProduct(ProductDTO productDTO, MultipartFile image) throws IOException, NoSuchElementException {
        Optional<Product> productOptional = productRepository.findById(productDTO.getId());
        if (productOptional.isEmpty()) {
            throw new NoSuchElementException("Product does not exist");
        }
        Optional<Category> category = categoryRepository.findById(productDTO.getCategory());
        if (category.isEmpty()) {
            throw new NoSuchElementException("Category does not exist");
        }
        try {
            Product product = productOptional.get();
            setProduct(productDTO, product, category.get());
            if (!image.isEmpty()) {
                String imageLink = imageService.saveImage(image, productDTO.getId(),false);
                imageService.deleteImage(product.getImageLink());
                product.setImageLink(imageLink);
            }
            productRepository.save(product);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public String getSuccessMessage() {
        return "Product updated successfully";
    }
}
