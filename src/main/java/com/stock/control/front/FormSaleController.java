package com.stock.control.front;

import com.stock.control.dto.ProductDTO;
import com.stock.control.dto.SaleDTO;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.SpringFXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

@Component
public class FormSaleController implements Initializable {

    @FXML
    private AnchorPane anchorFormSale;

    @FXML
    private Button btnGoBack, btnSave, btnSearchProduct;

    @FXML
    private Label lblAmount, lblDate, lblFinalPrice, lblPrice;

    @FXML
    private ListView<ProductDTO> listProducts;

    private SaleDTO saleDto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listProducts.itemsProperty().addListener(
                (obs, oldValue, newValue) -> updateLabels()
        );

        lblDate.setText(LocalDate.now().toString());

        saleDto = new SaleDTO();
        saleDto.setProducts(new ArrayList<>());
        ControllerManager.setFormSaleController(this);
    }

    @FXML
    public void openProductSearch(){
        try {
            SpringFXMLController.openNewWindowAndKeepCurrent(
                    SpringFXMLController.PATH_PRODUCT_SEARCH,
                    "Busqueda de Productos"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLabels(){
        lblPrice.setText(calculatePrice());
        lblAmount.setText(getTotalAmount());
        lblFinalPrice.setText(calculateFinalPrice());
    }

    private String calculatePrice(){
        return String.valueOf(
                saleDto.getProducts().stream()
                        .mapToDouble(product -> product.getPrice() * 5)
                        .sum()
        );
    }

    private String getTotalAmount(){
//        return String.valueOf(saleDto.getProducts().stream()
//                .mapToInt(ProductDTO::getAmountToSell)
//                .sum()
//        );

        return String.valueOf(new Random().nextInt(100 - 1) + 1);
    }

    private String calculateFinalPrice(){
        return String.valueOf( Double.parseDouble(lblPrice.getText()) * 1.21);
    }

    public void addProduct(ProductDTO productDto){
        saleDto.getProducts().add(productDto);
        listProducts.setItems(FXCollections.observableArrayList(saleDto.getProducts()));
    }
}
