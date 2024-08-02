package com.restful.product_crud.service;

import com.restful.product_crud.entity.Category;
import com.restful.product_crud.entity.Product;
import com.restful.product_crud.model.CategoryResponse;
import com.restful.product_crud.model.CreateProductRequest;
import com.restful.product_crud.model.ProductResponse;
import com.restful.product_crud.model.UpdateProductRequest;
import com.restful.product_crud.repository.CategoryRepository;
import com.restful.product_crud.repository.ProductRepository;
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private ProductCodeGenerator productCodeGenerator;

    @Transactional
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse.ProductResponseBuilder productResponseBuilder = ProductResponse.builder()
                    .id(product.getId())
                    .code(product.getCode())
                    .name(product.getName())
                    .price(product.getPrice());

            if (product.getCategory() != null) {
                productResponseBuilder.category(
                        CategoryResponse.builder()
                                .id(product.getCategory().getId())
                                .name(product.getCategory().getName())
                                .build()
                );
            }

            productResponses.add(productResponseBuilder.build());
        }

        return productResponses;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Set<ConstraintViolation<CreateProductRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        String code = request.getCode();
        if (code != null && !code.isBlank()) {
            if (!productCodeGenerator.validateCode(code)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product code. Must follow this format PXXXXX (eg. P00001).");
            }

            if (productRepository.existsByCode(request.getCode())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product code already exists.");
            }
        } else {
            code = productCodeGenerator.generate(1);
        }

        Category category = null;
        if (request.getCategoryID() > 0) {
            category = categoryRepository.findById(request.getCategoryID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found."));
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setCode(code);
        product.setPrice(request.getPrice());
        if (category != null) {
            product.setCategory(category);
        }

        productRepository.save(product);

        ProductResponse.ProductResponseBuilder productResponseBuilder = ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice());

        if (category != null) {
            productResponseBuilder.category(
                    CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .build()
            );
        }

        return productResponseBuilder.build();
    }

    @Transactional
    public ProductResponse find(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        ProductResponse.ProductResponseBuilder productResponseBuilder = ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice());

        if (product.getCategory() != null) {
            productResponseBuilder.category(
                    CategoryResponse.builder()
                            .id(product.getCategory().getId())
                            .name(product.getCategory().getName())
                            .build()
            );
        }

        return productResponseBuilder.build();
    }

    @Transactional
    public ProductResponse update(int id, UpdateProductRequest request) {
        Set<ConstraintViolation<UpdateProductRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        String code = request.getCode();
        if (code != null && !code.isBlank()) {
            if (!productCodeGenerator.validateCode(code)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product code. Must follow this format PXXXXX (eg. P00001).");
            }

            if (productRepository.existsByCodeAndIdNot(request.getCode(), id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product code already exists.");
            }
        } else {
            code = product.getCode();
        }

        Category category = null;
        if (request.getCategoryID() > 0) {
            category = categoryRepository.findById(request.getCategoryID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found."));
        }

        product.setCode(code);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        if (category != null) {
            product.setCategory(category);
        }

        productRepository.save(product);

        ProductResponse.ProductResponseBuilder productResponseBuilder = ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice());

        if (category != null) {
            productResponseBuilder.category(
                    CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .build()
            );
        }

        return productResponseBuilder.build();
    }

    @Transactional
    public void delete(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        productRepository.delete(product);
    }
}
