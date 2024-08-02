package com.restful.product_crud.controller;

import com.restful.product_crud.model.CreateProductRequest;
import com.restful.product_crud.model.ProductResponse;
import com.restful.product_crud.model.UpdateProductRequest;
import com.restful.product_crud.model.WebResponse;
import com.restful.product_crud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(
            path = "/products"
    )
    public WebResponse<List<ProductResponse>> list() {
        List<ProductResponse> productResponses = productService.list();
        return WebResponse.<List<ProductResponse>>builder()
                .status("success").data(productResponses).build();
    }

    @PostMapping(
            path = "/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> create(@RequestBody CreateProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        return WebResponse.<ProductResponse>builder()
                .status("success").data(productResponse).build();
    }

    @GetMapping(
            path = "/products/{id}"
    )
    public WebResponse<ProductResponse> find(@PathVariable("id") int id) {
        ProductResponse productResponse = productService.find(id);
        return WebResponse.<ProductResponse>builder()
                .status("success").data(productResponse).build();
    }

    @PutMapping(
            path = "/products/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> update(@PathVariable("id") int id, @RequestBody UpdateProductRequest request) {
        ProductResponse productResponse = productService.update(id, request);
        return WebResponse.<ProductResponse>builder()
                .status("success").data(productResponse).build();
    }

    @DeleteMapping(
            path = "/products/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("id") int id) {
        productService.delete(id);
        return WebResponse.<String>builder()
                .status("success").build();
    }
}
