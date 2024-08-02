package com.restful.product_crud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private int id;

    private String code;

    private String name;

    private double price;

    private CategoryResponse category;

}
