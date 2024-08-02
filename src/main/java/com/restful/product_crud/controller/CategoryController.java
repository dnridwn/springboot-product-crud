package com.restful.product_crud.controller;

import com.restful.product_crud.model.CategoryResponse;
import com.restful.product_crud.model.CreateCategoryRequest;
import com.restful.product_crud.model.UpdateCategoryRequest;
import com.restful.product_crud.model.WebResponse;
import com.restful.product_crud.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(
            path = "/categories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<CategoryResponse>> list() {
        List<CategoryResponse> categoryResponses = categoryService.list();
        return WebResponse.<List<CategoryResponse>>builder().status("success").data(categoryResponses).build();
    }

    @PostMapping(
            path = "/categories",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> create(@RequestBody CreateCategoryRequest request) {
        CategoryResponse categoryResponse = categoryService.create(request);
        return WebResponse.<CategoryResponse>builder().status("success").data(categoryResponse).build();
    }

    @GetMapping(
            path = "/categories/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> find(@PathVariable("id") int id) {
        CategoryResponse categoryResponse = categoryService.find(id);
        return WebResponse.<CategoryResponse>builder().status("success").data(categoryResponse).build();
    }

    @PutMapping(
            path = "/categories/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> update(@PathVariable("id") int id, @RequestBody UpdateCategoryRequest request) {
        CategoryResponse categoryResponse = categoryService.update(id, request);
        return WebResponse.<CategoryResponse>builder().status("success").data(categoryResponse).build();
    }

    @DeleteMapping(
            path = "/categories/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("id") int id) {
        categoryService.delete(id);
        return WebResponse.<String>builder().status("success").build();
    }
}
