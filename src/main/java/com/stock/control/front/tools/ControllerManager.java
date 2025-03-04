package com.stock.control.front.tools;

import com.stock.control.entity.Product;
import com.stock.control.front.StockControlController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class ControllerManager {

    @Getter @Setter
    private static StockControlController stockControlController;

    @Getter @Setter
    private static String formProductStatus;

    @Getter @Setter
    private static Product productToEdit;
}
