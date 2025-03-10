package com.stock.control.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    private String name;
    private Double price;
    private int stock;
    private int amountToSell;

    @Override
    public String toString(){
        return "Nombre: " + name + " \nPrecio: " + price + "$ \nStock Disponible: " + stock;
    }
}
