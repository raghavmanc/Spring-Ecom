package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFound;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories(){
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) throw new APIException("No Categories to fetch");
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) throw new APIException("Category with this name already exists");
        categoryRepository.save(category);
    }


    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();

        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow( () -> new ResourceNotFound("Category", "CategoryId",categoryId));

        categoryRepository.delete(category);
        return "Category " + category.getCategoryName() + " Deleted!";
    }

    @Override
    public String updateCategory(Category category, Long categoryId) {

        List<Category> categories = categoryRepository.findAll();

        Category tempCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow( () -> new ResourceNotFound("Category", "CategoryId",categoryId));

        String oldCategoryName = tempCategory.getCategoryName();
        tempCategory.setCategoryName(category.getCategoryName());
        categoryRepository.save(tempCategory);

        return "Category with id " + categoryId + " name changed from "+ oldCategoryName + " to " + tempCategory.getCategoryName();
    }

}