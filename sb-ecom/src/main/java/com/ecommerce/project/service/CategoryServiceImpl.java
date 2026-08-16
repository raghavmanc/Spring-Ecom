package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFound;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ext.sql.JavaSqlTimestampDeserializer;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getCategories(){
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) throw new APIException("No Categories to fetch");

        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        return new CategoryResponse(categoryDTOS);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb != null) throw new APIException("Category with this name already exists");

        return modelMapper.map(categoryRepository.save(category),CategoryDTO.class);
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