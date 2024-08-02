package com.restful.product_crud.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse <T> {

    private String status;

    private String message;

    private T data;
}
