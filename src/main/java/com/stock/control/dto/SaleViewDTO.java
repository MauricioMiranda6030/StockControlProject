package com.stock.control.dto;

import com.stock.control.entity.Sale;
import com.stock.control.entity.SaleDetails;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SaleViewDTO {

    private Long id;
    private String productsDetails, totalAmount, finalPrice, dateOfSale;


    public SaleViewDTO(Sale sale, List<SaleDetails> saleDetails){
        this.id = sale.getId();
        this.dateOfSale = sale.getDateOfSale().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.finalPrice = String.format("%.2f$", sale.getFinalPrice());
        this.totalAmount = sale.getTotalAmount() + " Unidades";

        productsDetails = saleDetails.stream()
                .map(sd -> "(" + sd.getQuantity() + ") " + sd.getProduct().getName())
                .collect(Collectors.joining(",\n"));
    }
}
