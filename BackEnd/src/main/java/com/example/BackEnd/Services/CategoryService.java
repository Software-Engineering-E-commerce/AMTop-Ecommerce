package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.CategoryDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            String imageLink = imageService.saveImage(image, category.getCategoryName(), true);
            category.setImageLink(imageLink);
            System.out.println("Image link: " + imageLink);
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
    public void editCategory(CategoryDTO categoryDTO, MultipartFile image, String originalName) throws IOException, NoSuchElementException {

        Optional<Category> categoryOptional = categoryRepository.findByCategoryName(categoryDTO.getName());
        if (categoryOptional.isPresent() && !originalName.equals(categoryDTO.getName())) {
            throw new NoSuchElementException("There's a category with this name already exists");
        }
        try {
            Optional<Category> editedCategory = categoryRepository.findByCategoryName(originalName);
            if (!image.isEmpty() && originalName.equals(categoryDTO.getName())) {
                String imageLink = imageService.saveImage(image, categoryDTO.getName(), true);
                imageService.deleteImage(editedCategory.get().getImageLink());
                editedCategory.get().setImageLink(imageLink);
            } else if (!image.isEmpty() && !originalName.equals(categoryDTO.getName())){
                Category newCategory = new Category();
                newCategory.setCategoryName(categoryDTO.getName());
                String imageLink = imageService.saveImage(image, categoryDTO.getName(), true);
                newCategory.setImageLink(imageLink);
                imageService.deleteImage(editedCategory.get().getImageLink());
                categoryRepository.delete(editedCategory.get());
                categoryRepository.save(newCategory);
            } else if (image.isEmpty() && !originalName.equals(categoryDTO.getName())){
                Category newCategory = new Category();
                newCategory.setCategoryName(categoryDTO.getName());
                String imageLink = editedCategory.get().getImageLink();
                newCategory.setImageLink(imageLink);
                categoryRepository.delete(editedCategory.get());
                categoryRepository.save(newCategory);
            } else {
                throw new NoSuchElementException("You've changed nothing !");
            }
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

    public List<CategoryDTO> getAllCategories() {
        List <CategoryDTO> categoryDTOList = new ArrayList<>();
        List<Category> categoryList = categoryRepository.findAll();
        for (Category category : categoryList) categoryDTOList.add(mapToDTO(category));
        return categoryDTOList;
    }

    private CategoryDTO mapToDTO(Category category) {
        return CategoryDTO.builder().name(category.getCategoryName()).imageUrl(category.getImageLink()).build();
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
