package com.stock.control.front;

import com.stock.control.dto.ProductDTO;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.service.IProductService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ProductSearchController implements Initializable {

    @FXML
    private Pane anchorSearch;

    @FXML
    private ListView<ProductDTO> listProducts;

    @FXML
    private TextField txtName;

    @Autowired
    private IProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtName.textProperty().addListener(
                (obs, oldValue, newValue) ->getProducts()
        );

        getProducts();
    }


    @FXML
    public void addProduct(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            ControllerManager.getFormSaleController().addProduct(listProducts.getFocusModel().getFocusedItem());
        }
    }

    private void getProducts(){
        listProducts.setItems(FXCollections.observableArrayList(productService.getProductsDtoByName(txtName.getText())));
    }

}
