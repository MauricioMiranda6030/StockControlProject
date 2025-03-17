package com.stock.control.front.tools;

import com.stock.control.entity.Product;
import com.stock.control.front.FormSaleController;
import com.stock.control.front.ProductSearchController;
import com.stock.control.front.SalesRecordController;
import com.stock.control.front.StockControlController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


/*
* Clase creada para pasar instancias entre controladores de FXML
*/
@Component
public class ControllerManager {

    @Getter @Setter
    private static StockControlController stockControlController;

    @Getter @Setter
    private static FormSaleController formSaleController;

    @Getter @Setter
    private static SalesRecordController salesRecordController;

    @Getter @Setter
    private static ProductSearchController productSearchController;

    @Getter @Setter
    private static String formProductStatus;

    @Getter @Setter
    private static Product productToEdit;
}
