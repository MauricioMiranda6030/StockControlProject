package com.stock.control.dto;

import com.stock.control.entity.Sale;
import com.stock.control.entity.SaleDetails;
import com.stock.control.front.tools.CurrencyFormater;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@Setter
public class SaleViewDTO {

    private Long id;
    private String productsDetails, totalAmount, finalPrice, dateOfSale, client, code;


    public SaleViewDTO(Sale sale, List<SaleDetails> saleDetails){
        this.id = sale.getId();
        this.dateOfSale = sale.getDateOfSale().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.finalPrice = CurrencyFormater.getCurrency(sale.getFinalPrice());
        this.totalAmount = sale.getTotalAmount() + " Unidades";
        this.client = sale.getClientFullName();
        this.code = sale.getDocId();

        productsDetails = saleDetails.stream()
                .map(sd -> "(" + sd.getQuantity() + ") " + sd.getProduct().getName() + " vendido/s a " + CurrencyFormater.getCurrency(sd.getSoldPrice()))
                .collect(Collectors.joining(",\n"));
    }
}
