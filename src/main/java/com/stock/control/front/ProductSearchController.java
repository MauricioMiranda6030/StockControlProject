package com.stock.control.front;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.WindowsManager;
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
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ProductSearchController implements Initializable {

    @FXML
    private Pane anchorSearch;

    @FXML
    private ListView<ProductForSaleDTO> listProducts;

    @FXML
    private TextField txtName;

    @FXML
    private Pane topBar;

    @Autowired
    private IProductService productService;

    @Getter
    private Stage thisWindowStage;

    private Double x = 0d, y = 0d;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> thisWindowStage = (Stage) anchorSearch.getScene().getWindow());
        ControllerManager.setProductSearchController(this);
        setListenerToTxtField();
        getProducts();

        moveWindowToTheRight();
        setMovementToTopBar();

    }

    private void moveWindowToTheRight() {
        Platform.runLater(() -> {
            Stage stage = thisWindowStage;
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
            ProductForSaleDTO productForSaleDTO = listProducts.getFocusModel().getFocusedItem();

            if (productForSaleDTO.getStock() == 0)
                ControlFXManager.buildNotification(
                        "¡Producto sin Stock! Añadir más Stock para realizar la compra",
                        "¡Advertencia!")
                .showWarning();
            else
                ControllerManager.getFormSaleController().addProduct(productForSaleDTO);
        }
    }

    public void getProducts(){
        listProducts.setItems(FXCollections.observableArrayList(productService.getProductsDtoByName(txtName.getText())));
    }

    private void setMovementToTopBar() {
        topBar.setOnMousePressed(event -> {
            x = event.getScreenX() - thisWindowStage.getX();
            y = event.getScreenY() - thisWindowStage.getY();
        });
        topBar.setOnMouseDragged(event -> WindowsManager.moveWindow(thisWindowStage, event, x, y));
    }

    @FXML
    private void minWindow(){
        WindowsManager.minWindow(thisWindowStage);
    }

    @FXML
    private void closeThisForm(){
        WindowsManager.closeWindow(thisWindowStage);
    }
}
