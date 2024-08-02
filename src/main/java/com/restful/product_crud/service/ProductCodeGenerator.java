package com.restful.product_crud.service;

import com.restful.product_crud.entity.Product;
import com.restful.product_crud.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.IllegalFormatException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProductCodeGenerator {

    @Autowired
    private ProductRepository productRepository;

    private final String defaultCode = "P00001";
    private final String format = "^P\\d{5}$";

    @Transactional
    public String generate(int additionNumber) {
        String code = defaultCode;

        Optional<Product> latestProduct = productRepository.findFirstByOrderByIdDesc();
        if (latestProduct.isPresent()) {
            code = latestProduct.get().getCode();
            code = code.substring(1, code.length()-1);

            int codeInInt;
            try {
                codeInInt = Integer.parseInt(code);
                codeInInt += additionNumber;
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating product code. 1");
            }

            try {
                code = String.format("P%5s", codeInInt).replace(" ", "0");
            } catch (IllegalFormatException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating product code. 2");
            }
        }

        if (productRepository.existsByCode(code)) {
            return generate(++additionNumber);
        }

        return code;
    }

    public boolean validateCode(String code) {
        return Pattern.matches(format, code);
    }

}
