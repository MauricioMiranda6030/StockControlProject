package com.stock.control.front.tools;

public class CleanFormat {
    public static String cleanPrice(String price){
        return price.replaceAll(" ", "")
                .replaceAll("\\.", "")
                .replace("$", "")
                .replace(",", ".");
    }
}
