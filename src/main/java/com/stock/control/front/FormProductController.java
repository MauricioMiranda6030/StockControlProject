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
import java.util.List;
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

    @FXML
    private void saveProduct(){

        if(!validateNotNullFields())
            System.out.println("ingresar todos los valores obligatorios");
        else if (lblWarningPrice.isVisible() || lblWarningStock.isVisible())
            System.out.println("ingresa valores numericos correctos en los campos price y  stock");
        else{
            setProduct();
            productService.saveProduct(product);
            resetTextFields();
            //todo implementar controlfx
            //todo actualizar la lista de la otra ventana
            System.out.println("se guardo bien el ak");
        }
    }


    private boolean validateNumbers(TextField field, Label labelToShow) {
        //Verifica que los valores ingresados sean numeros o un .
        boolean result = field.getText().chars().allMatch( (c) -> (c >= 48 && c <= 56) || c == 46 );
        labelToShow.setVisible(!result);

        return result;
    }

    private boolean validateNotNullFields(){
        List<TextField> fieldList = List.of(txtName, txtPrice, txtStock);
        return fieldList.stream().noneMatch( (field) -> field.getText().isEmpty() );
    }

    private void setProduct(){
        product.setName(txtName.getText());
        product.setPrice(Double.valueOf(txtPrice.getText()));
        product.setStock(Integer.valueOf(txtStock.getText()));
        product.setDescription(txtDescription.getText());
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
