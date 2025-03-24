package com.stock.control.front.tools;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormater{
    private static final NumberFormat instance = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    public static String getCurrency(Double price){
        return instance.format(price);
    }
}
