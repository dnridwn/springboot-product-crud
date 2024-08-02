package com.restful.product_crud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restful.product_crud.entity.Category;
import com.restful.product_crud.model.CreateCategoryRequest;
import com.restful.product_crud.model.CategoryResponse;
import com.restful.product_crud.model.UpdateCategoryRequest;
import com.restful.product_crud.model.WebResponse;
import com.restful.product_crud.repository.CategoryRepository;
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
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();

        entityManager.createNativeQuery("ALTER TABLE categories AUTO_INCREMENT = 1").executeUpdate();

        Category category = new Category();
        category.setId(1);
        category.setName("Category A");
        categoryRepository.save(category);
    }

//    @AfterEach
//    void tearDown() {
//        categoryRepository.deleteAll();
//
//        entityManager.createNativeQuery("ALTER TABLE categories AUTO_INCREMENT = 1").executeUpdate();
//    }

    @Test
    void testListSuccess() throws Exception {
        mockMvc.perform(
                get("/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<CategoryResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testCreateSuccess() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Category B");

        mockMvc.perform(
                post("/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testCreateFailedWithoutName() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest();

        mockMvc.perform(
                post("/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<List<CategoryResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("name: Name cannot be empty.", response.getMessage());
        });
    }

    @Test
    void testFindSuccess() throws Exception {
        int id = 1;
        mockMvc.perform(
                get("/categories/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testFindFailedCategoryNotFound() throws Exception {
        int id = 2;
        mockMvc.perform(
                get("/categories/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
        });
    }

    @Test
    void testUpdateSuccess() throws Exception {
        int id = 1;
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Category B");

        mockMvc.perform(
                put("/categories/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("success", response.getStatus());
        });
    }

    @Test
    void testUpdateFailedWithoutName() throws Exception {
        int id = 1;
        UpdateCategoryRequest request = new UpdateCategoryRequest();

        mockMvc.perform(
                put("/categories/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<List<CategoryResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals("failed", response.getStatus());
            assertEquals("name: Name cannot be empty.", response.getMessage());
        });
    }

    @Test
    void testDeleteSuccess() throws Exception {
        int id = 1;
        mockMvc.perform(
                delete("/categories/" + id)
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
    void testDeleteFailedCategoryNotFound() throws Exception {
        int id = 2;
        mockMvc.perform(
                delete("/categories/" + id)
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
