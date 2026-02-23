package com.stock.control.dto;

import lombok.Data;

@Data
public class ProductSaveDTO {
    private Long id;
    private String name, description;
    private int stock;
    private Double price;
}
