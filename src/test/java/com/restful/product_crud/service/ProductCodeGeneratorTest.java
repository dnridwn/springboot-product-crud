package com.restful.product_crud.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCodeGeneratorTest {

    @Autowired
    private ProductCodeGenerator productCodeGenerator;

    @Test
    void testValidateCodeSuccess() {
        assertTrue(productCodeGenerator.validateCode("P00001"));
    }

    @Test
    void testValidateCodeFailed() {
        assertFalse(productCodeGenerator.validateCode("PD0001"));
    }
}