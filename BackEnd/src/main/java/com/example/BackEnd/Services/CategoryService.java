package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.CategoryDTO;
import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Repositories.CategoryRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    /**
     * This method is for adding a new category by the admin.
     * The category is added if it wasn't already exist
     *
     * @param categoryDTO holds the category information collected from view layer(i.e. category name and image link).
     * @param image       the image for the new category.
     */
    public void addCategory(CategoryDTO categoryDTO, MultipartFile image) throws IOException, Exception {
        Category category = new Category();
        if (categoryRepository.findByCategoryName(categoryDTO.getName()).isPresent()) {
            throw new Exception("Category " + categoryDTO.getName() + " is already exist");
        }
        try {
            category.setCategoryName(categoryDTO.getName());
            categoryRepository.save(category);
            String imageLink = imageService.saveImage(image, category.getCategoryName(), true);
            category.setImageLink(imageLink);
            categoryRepository.save(category);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * This method is for editing a new category by the admin.
     * The category is updated if it is existing.
     *
     * @param categoryDTO holds the category information collected from view layer(i.e. category name and image link).
     * @param image       the new image for the updated category.
     */
    public void editCategory(CategoryDTO categoryDTO, MultipartFile image) throws IOException, NoSuchElementException {
        Optional<Category> categoryOptional = categoryRepository.findByCategoryName(categoryDTO.getName());
        if (categoryOptional.isEmpty()) {
            throw new NoSuchElementException("Category does not exist");
        }
        try {

            Category category = categoryOptional.get();
            category.setCategoryName(categoryDTO.getName());
            if (!image.isEmpty()) {
                String imageLink = imageService.saveImage(image, categoryDTO.getName(), true);
                imageService.deleteImage(category.getImageLink());
                category.setImageLink(imageLink);
            }
            categoryRepository.save(category);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * This method is for retrieving a category with the specified name.
     *
     * @param categoryName holds the category information collected from view layer(i.e. category name and image link).
     * @return the category with this specific name.
     */
    public CategoryDTO getCategory(String categoryName) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();

                return CategoryDTO.builder()
                                .name(category.getCategoryName())
                                .imageUrl(category.getImageLink())
                                .build();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method is for getting a message indicates successful process (add category or edit category)
     *
     * @param addCategory is true if the admin adds a category and false if it edits a category
     * @return the suitable success message based on addOrEdit boolean
     */
    public String getSuccessMessage(boolean addCategory) {
        return addCategory ? "Category added successfully" : "Category edited successfully";
    }
}
