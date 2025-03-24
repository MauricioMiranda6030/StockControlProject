package com.stock.control.dto;

import com.stock.control.front.tools.CurrencyFormater;
import lombok.Data;

import java.text.NumberFormat;
import java.util.Locale;

@Data
public class ProductForSaleDTO {
    private long id;
    private String name;
    private Double price;
    private int stock;
    private int amountToSell = 1;

    @Override
    public String toString(){
        return "Nombre: " + name + " \n" +
                "Precio: " + CurrencyFormater.getCurrency(price) + "\n" +
                "Stock Disponible: " + stock;
    }
}
