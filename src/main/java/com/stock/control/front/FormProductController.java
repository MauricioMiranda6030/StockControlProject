package com.stock.control.front;

import com.stock.control.entity.Product;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.IProductService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class FormProductController implements Initializable {

    private static FormProductController instance = null;

    private FormProductController(){}

    //Patron singleton
    public static synchronized FormProductController getInstance(){
        if(instance == null)
            instance = new FormProductController();
        return instance;
    }

    @Autowired
    private IProductService productService;

    @FXML
    private AnchorPane productAnchorPane;

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
        setListenersToTxtFields();
        setUpForm();
    }

    private void setUpForm() {
        if(ControllerManager.getFormProductStatus().equals("save"))
            setUpForSave();
        else
            setUpForEdit();
    }

    private void setListenersToTxtFields() {
        txtPrice.textProperty().addListener(
                (obs, oldValue, newValue) -> validateNumbers(txtPrice, lblWarningPrice)
        );

        txtStock.textProperty().addListener(
                (obs, oldValue, newValue) -> validateNumbers(txtStock, lblWarningStock)
        );
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
            if(confirmationDialog().get() == ButtonType.OK){
                setAndSave();
                ControllerManager.getStockControlController().getProducts();

                resetTextFields();
                ControlFXManager.buildNotification("/images/check.png", "Producto guardado correctamente", "Registro de Producto")
                        .show();
            }
        }
    }

    @FXML
    private void editProduct(){
        if(validateAll()){
            if(confirmationDialog().get() == ButtonType.OK){
                setAndSave();
                ControllerManager.getStockControlController().getProducts();
                closeThisForm();
            }
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

    private Optional<ButtonType> confirmationDialog(){
        return WindowsManager.confirmDialog(productAnchorPane, "Confirmación de Datos: ¿Esta Seguro?");
    }
}
