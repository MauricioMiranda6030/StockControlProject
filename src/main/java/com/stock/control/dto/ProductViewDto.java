package com.stock.control.dto;

import lombok.Data;

@Data
public class ProductViewDto {
    private Long id;
    private String name, description, stock, price;
}
