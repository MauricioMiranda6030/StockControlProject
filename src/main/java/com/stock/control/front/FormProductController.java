package com.stock.control.front;

import com.stock.control.entity.Product;
import com.stock.control.service.IProductService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@Component
public class FormProductController implements Initializable {

    @Autowired
    private IProductService productService;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtName, txtPrice, txtStock;

    @FXML
    private Button btnClose, btnSave, btnEdit;

    @FXML
    private Label lblWarningPrice,lblWarningStock, lblTitle ;

    private Product product;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        txtPrice.textProperty().addListener(
                (obs, oldValue, newValue) -> validateNumbers(txtPrice, lblWarningPrice)
        );

        txtStock.textProperty().addListener(
                (obs, oldValue, newValue) -> validateNumbers(txtStock, lblWarningStock)
        );

        if(ControllerManager.getFormProductStatus().equals("save"))
            setUpForSave();
        else
            setUpForEdit();
    }

    private void setUpForSave(){
        product = new Product();
        lblTitle.setText("Nuevo Producto");
        btnSave.setVisible(true);
        btnEdit.setVisible(false);
    }

    private void setUpForEdit(){
        product = ControllerManager.getProductToEdit();
        lblTitle.setText("Editar Producto");

        setUpFieldsForEdit();

        btnSave.setVisible(false);
        btnEdit.setVisible(true);
    }

    private void setUpFieldsForEdit(){
        txtName.setText(product.getName());
        txtPrice.setText(String.valueOf(product.getPrice()));
        txtStock.setText(String.valueOf(product.getStock()));
        txtDescription.setText(product.getDescription());
    }

    @FXML
    private void saveProduct(){
        if (validateAll()) {
            setAndSave();
            ControllerManager.getMainController().getProducts();

            resetTextFields();
            ControlFXManager.buildNotification("/images/check.png", "Producto guardado correctamente", "Registro de Producto")
                    .show();
        }
    }

    @FXML
    private void editProduct(){
        if(validateAll()){
            setAndSave();
            ControllerManager.getMainController().getProducts();
            closeThisForm();
        }
    }

    private void setAndSave(){
        System.out.println("setting and saving");
        setProduct();
        productService.saveProduct(product);
    }

    private boolean validateAll(){
        if(!validateNotNullFields()){
            ControlFXManager.buildNotification("Debe haber valores en los campos obligatorios", "Campos Vacios")
                    .showError();
            return false;
        }
        else if (validateNoNumberError()){
            ControlFXManager.buildNotification("Debes ingresar correctamente valores númericos en Precio y Stock","Producto No guardado")
                    .showWarning();
            return false;
        }
        return true;
    }

    private boolean validateNoNumberError(){
        return lblWarningPrice.isVisible() || lblWarningStock.isVisible();
    }

    private void validateNumbers(TextField field, Label labelToShow) {
        //Verifica que los valores ingresados sean numeros o un .
        boolean result = field.getText().chars().allMatch( (c) -> (c >= 48 && c <= 56) || c == 46 );
        labelToShow.setVisible(!result);

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
