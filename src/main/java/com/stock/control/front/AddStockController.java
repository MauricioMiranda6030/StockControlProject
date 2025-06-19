package com.stock.control.front;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.IProductService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class AddStockController implements Initializable {

    @FXML
    private AnchorPane addStockAnchorPane;

    @FXML
    private Label lblCurrentStock;

    @FXML
    private Label lblFinalStock;

    @FXML
    private TextField txtNewStock;

    @FXML
    private Button btnAddStock, btnClose;

    private Integer currentStock, newStock;

    private Stage thisWindowStage;

    @Autowired
    private IProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            thisWindowStage = (Stage) addStockAnchorPane.getScene().getWindow();
        });
        setUpVariables();
        setUpTxtStock();
        setUpButtons();
    }

    private void setUpButtons() {
        btnAddStock.setOnAction(updateStock());
        btnClose.setOnAction(e -> closeThisForm());
    }

    private EventHandler<ActionEvent> updateStock() {
        return e -> {
            ProductForSaleDTO productDto = new ProductForSaleDTO();
            productDto.setId(ControllerManager.getProductToAddStock().getId());
            productDto.setStock(Integer.parseInt(lblFinalStock.getText()));

            productService.addStock(productDto);
            ControllerManager.getStockControlController().getProducts();
            closeThisForm();
        };
    }

    private void setUpTxtStock() {
        txtNewStock.textProperty().addListener(
                (obs, oldValue, newValue) -> {
                    if (!newValue.isBlank())
                    {
                        btnAddStock.setDisable(false);
                        try{
                            newStock = Integer.valueOf(txtNewStock.getText());
                            lblFinalStock.setText(calculateFinalStock());
                        }catch (Exception e){
                            txtNewStock.setText(oldValue);
                        }
                    }else
                        btnAddStock.setDisable(true);
                }
        );
    }

    private void setUpVariables() {
        currentStock = ControllerManager.getProductToAddStock().getStock();
        lblCurrentStock.setText(String.valueOf(currentStock));
        newStock = 1;
        txtNewStock.setText("1");
        lblFinalStock.setText(calculateFinalStock());
    }

    private String calculateFinalStock(){
        return String.valueOf(currentStock + newStock);
    }

    private void closeThisForm(){
        WindowsManager.closeWindow(thisWindowStage);
    }
}
