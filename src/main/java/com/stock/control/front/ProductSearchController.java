package com.stock.control.front;

import com.stock.control.dto.ProductDTO;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.service.IProductService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
        moveWindowToTheRight();
        setListenerToTxtField();
        ControllerManager.setProductSearchController(this);
        getProducts();
    }

    private void moveWindowToTheRight() {
        Platform.runLater(() -> {
            Stage stage = (Stage) anchorSearch.getScene().getWindow();
            stage.setX(1400);
        });
    }

    private void setListenerToTxtField() {
        txtName.textProperty().addListener(
                (obs, oldValue, newValue) ->getProducts()
        );
    }


    @FXML
    public void addProduct(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            ProductDTO productDTO = listProducts.getFocusModel().getFocusedItem();

            if (productDTO.getStock() == 0)
                ControlFXManager.buildNotification(
                        "¡Producto sin Stock! Añadir más Stock para realizar la compra",
                        "¡Advertencia!")
                .showWarning();
            else
                ControllerManager.getFormSaleController().addProduct(productDTO);
        }
    }

    public void getProducts(){
        listProducts.setItems(FXCollections.observableArrayList(productService.getProductsDtoByName(txtName.getText())));
    }

}
