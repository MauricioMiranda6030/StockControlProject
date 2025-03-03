package com.stock.control.front;

import com.stock.control.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class ControllerManager {

    @Getter @Setter
    private static MainController mainController;

    @Getter @Setter
    private static String formProductStatus;

    @Getter @Setter
    private static Product productToEdit;
}
