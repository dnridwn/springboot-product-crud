package com.restful.product_crud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restful.product_crud.entity.Category;
import com.restful.product_crud.entity.Product;
import com.restful.product_crud.model.CreateProductRequest;
import com.restful.product_crud.model.ProductResponse;
import com.restful.product_crud.model.UpdateProductRequest;
import com.restful.product_crud.model.WebResponse;
import com.restful.product_crud.repository.CategoryRepository;
import com.restful.product_crud.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        entityManager.createNativeQuery("ALTER TABLE products AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE categories AUTO_INCREMENT = 1").executeUpdate();

        Category category = new Category();
        category.setId(1);
        category.setName("Category A");
        categoryRepository.save(category);

        Product product = new Product();
        product.setId(1);
        product.setCode("P00001");
        product.setName("Product A");
        product.setPrice((double) 1000);
        product.setCategory(category);
        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        entityManager.createNativeQuery("ALTER TABLE products AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE categories AUTO_INCREMENT = 1").executeUpdate();
    }

    @Test
    void testListSuccess() throws Exception {
        mockMvc.perform(
                get("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ProductResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testCreateSuccess() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Product B");
        request.setCode("P00002");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testCreateSuccessWithoutCategory() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Product B");
        request.setCode("P00002");
        request.setPrice((double) 1000);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testCreateSuccessWithoutCode() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Product B");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testCreateFailedCategoryNotFound() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Product B");
        request.setCode("P00002");
        request.setPrice((double) 1000);
        request.setCategoryID(2);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("Category not found.", response.getMessage());
        });
    }

    @Test
    void testCreateFailedInvalidCode() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Product A");
        request.setCode("P00X01");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("Invalid product code. Must follow this format PXXXXX (eg. P00001).", response.getMessage());
        });
    }

    @Test
    void testCreateFailedCodeAlreadyUsed() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Product A");
        request.setCode("P00001");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("Product code already exists.", response.getMessage());
        });
    }

    @Test
    void testCreateFailedWithoutName() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setCode("P00001");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("name: Name cannot be empty.", response.getMessage());
        });
    }

    @Test
    void testCreateFailedWithoutPrice() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setCode("P00001");
        request.setName("Product A");
        request.setCategoryID(1);

        mockMvc.perform(
                post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("price: Price cannot be empty.", response.getMessage());
        });
    }

    @Test
    void testFindSuccess() throws Exception {
        int id = 1;

        mockMvc.perform(
                get("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testFindFailedProductNotFound() throws Exception {
        int id = 2;

        mockMvc.perform(
                get("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
        });
    }

    @Test
    void testUpdateSuccess() throws Exception {
        int id = 1;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setCode("P00001");
        request.setName("Product A (Updated)");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testUpdateSuccessWithoutCategory() throws Exception {
        int id = 1;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Product A");
        request.setCode("P00001");
        request.setPrice((double) 1000);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testUpdateSuccessWithoutCode() throws Exception {
        int id = 1;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Product A");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testUpdateFailedCategoryNotFound() throws Exception {
        int id = 1;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Product B");
        request.setCode("P00002");
        request.setPrice((double) 1000);
        request.setCategoryID(2);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("Category not found.", response.getMessage());
        });
    }

    @Test
    void testUpdateFailedInvalidCode() throws Exception {
        int id = 1;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Product A");
        request.setCode("P00X01");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("Invalid product code. Must follow this format PXXXXX (eg. P00001).", response.getMessage());
        });
    }

    @Test
    void testUpdateFailedCodeAlreadyUsed() throws Exception {
        Product product = new Product();
        product.setId(2);
        product.setCode("P00002");
        product.setName("Product B");
        product.setPrice((double) 1000);
        productRepository.save(product);

        int id = 2;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setCode("P00001");
        request.setName("Product B (Updated)");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("Product code already exists.", response.getMessage());
        });
    }

    @Test
    void testUpdateFailedWithoutName() throws Exception {
        int id = 1;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setCode("P00001");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("name: Name cannot be empty.", response.getMessage());
        });
    }

    @Test
    void testUpdateFailedWithoutPrice() throws Exception {
        int id = 1;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setCode("P00001");
        request.setName("Product A (updated)");
        request.setCategoryID(1);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("price: Price cannot be empty.", response.getMessage());
        });
    }

    @Test
    void testUpdateFailedProductNotFound() throws Exception {
        int id = 2;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setCode("P00001");
        request.setName("Product A (updated)");
        request.setPrice((double) 1000);
        request.setCategoryID(1);

        mockMvc.perform(
                put("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("Product not found.", response.getMessage());
        });
    }

    @Test
    void testDeleteSuccess() throws Exception {
        int id = 1;

        mockMvc.perform(
                delete("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testDeleteFailedNotFound() throws Exception {
        int id = 2;

        mockMvc.perform(
                delete("/products/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
        });
    }
}