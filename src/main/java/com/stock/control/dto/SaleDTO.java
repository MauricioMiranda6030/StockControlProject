package com.stock.control.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaleDTO {
    private Double finalPrice;
    private int totalAmount;
    private List<ProductForSaleDTO> products;
}
