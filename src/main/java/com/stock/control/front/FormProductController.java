package com.stock.control.front;

import com.stock.control.entity.Product;
import com.stock.control.service.IProductService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class FormProductController implements Initializable {

    @Autowired
    private IProductService productService;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtStock;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnSave;

    @FXML
    private Label lblWarningPrice;

    @FXML
    private Label lblWarningStock;

    private Product product;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        product = new Product();

        txtPrice.textProperty().addListener(
                (obs, oldValue, newValue) -> validateNumbers(txtPrice, lblWarningPrice)
        );

        txtStock.textProperty().addListener(
                (obs, oldValue, newValue) -> validateNumbers(txtStock, lblWarningStock)
        );
    }

    private boolean validateNumbers(TextField field, Label labelToShow) {
        //Verifica que los valores ingresados sean numeros o un .
        boolean result = field.getText().chars().allMatch( (c) -> (c >= 48 && c <= 56) || c == 46 );
        labelToShow.setVisible(!result);

        return result;
    }

    @FXML
    private void saveProduct(){

        String name = txtName.getText();
        String description = txtDescription.getText();
        Double price = Double.valueOf(txtPrice.getText());
        Integer stock = Integer.valueOf(txtStock.getText());

        if(name.isBlank() || stock == null || price == null || description.isBlank())
            //todo mensaje de llenar campos
            System.out.println("hace algo lope");
        else{
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setDescription(description);

            productService.saveProduct(product);
            resetTextFields();

            //todo pop up de se guardo
            //todo refrescar la otra lista
        }
    }

    @FXML
    private void resetTextFields(){
        txtDescription.clear();
        txtName.clear();
        txtPrice.clear();
        txtStock.clear();
    }

    @FXML
    private void closeThisForm(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
