package com.restful.product_crud.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {

    @Size(max = 100, message = "Code too long. Maximum 100 characters.")
    private String code;

    @NotBlank(message = "Name cannot be empty.")
    @Size(max = 200, message = "Name too long. Maximum 200 characters.")
    private String name;

    @NotNull(message = "Price cannot be empty.")
    private Double price;

    private int categoryID;

}
