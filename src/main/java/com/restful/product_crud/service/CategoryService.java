package com.restful.product_crud.service;

import com.restful.product_crud.entity.Category;
import com.restful.product_crud.model.CategoryResponse;
import com.restful.product_crud.model.CreateCategoryRequest;
import com.restful.product_crud.model.CreateProductRequest;
import com.restful.product_crud.model.UpdateCategoryRequest;
import com.restful.product_crud.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public List<CategoryResponse> list() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category category : categories) {
            categoryResponses.add(
                CategoryResponse.builder().id(category.getId()).name(category.getName()).build()
            );
        }

        return categoryResponses;
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        Set<ConstraintViolation<CreateCategoryRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Category category = new Category();
        category.setName(request.getName());

        categoryRepository.save(category);

        return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
    }

    @Transactional
    public CategoryResponse find(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
    }

    @Transactional
    public CategoryResponse update(int id, UpdateCategoryRequest request) {
        Set<ConstraintViolation<UpdateCategoryRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(request.getName());

        categoryRepository.save(category);

        return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
    }

    @Transactional
    public void delete(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(category);
    }
}
