package com.stock.control.front;

import com.stock.control.dto.ProductSaveDto;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.IProductService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class FormProductController implements Initializable {

    @Autowired
    private IProductService productService;

    private final Logger log = LoggerFactory.getLogger(FormProductController.class);

    @FXML
    private AnchorPane productAnchorPane;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtName, txtPrice, txtStock;

    @FXML
    private Button btnSave, btnEdit;

    @FXML
    private Label lblWarningPrice,lblWarningStock, lblTitle ;

    @FXML
    private Pane topBar;

    private ProductSaveDto productDto;

    private Double x = 0d, y = 0d;

    @Getter
    private Stage thisWindowStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> thisWindowStage = (Stage) productAnchorPane.getScene().getWindow());
        setListenersToTxtFields();
        setUpForm();
        ControllerManager.setFormProductController(this);
        setMovementToTopBar();
    }

    private void setMovementToTopBar() {
        topBar.setOnMousePressed(event -> {
            x = event.getScreenX() - thisWindowStage.getX();
            y = event.getScreenY() - thisWindowStage.getY();
        });
        topBar.setOnMouseDragged(event -> WindowsManager.moveWindow(thisWindowStage, event, x, y));
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
        productDto = new ProductSaveDto();
        lblTitle.setText("Nuevo Producto");
        btnSave.setVisible(true);
        btnEdit.setVisible(false);
    }

    private void setUpForEdit(){
        productDto = ControllerManager.getProductToEdit();
        lblTitle.setText("Editar Producto");

        setUpFieldsForEdit();

        btnSave.setVisible(false);
        btnEdit.setVisible(true);
    }

    private void setUpFieldsForEdit(){
        txtName.setText(productDto.getName());
        txtPrice.setText(String.valueOf(productDto.getPrice()));
        txtStock.setText(String.valueOf(productDto.getStock()));
        txtDescription.setText(productDto.getDescription());
    }

    @FXML
    private void saveProduct(){
        if (validateAll()) {
            if(confirmationDialog().get() == ButtonType.OK){
                setAndSave();
                updateProductRegisterTable();
                resetTextFields();

                log.info("New product just saved: {}", productDto.getName());
                productDto = new ProductSaveDto();
                ControlFXManager.buildNotification("/images/check.png", "Producto guardado correctamente", "Registro de Producto")
                        .show();
            }
        }
    }

    private static void updateProductRegisterTable() {
        ControllerManager.getStockControlController().getProducts();
    }

    @FXML
    private void editProduct(){
        if(validateAll()){
            if(confirmationDialog().get() == ButtonType.OK){
                setAndSave();
                updateProductRegisterTable();
                closeThisForm();

                log.info("Updating product with id: {}", productDto.getId());
            }
        }
    }

    private void setAndSave(){
        setProductDto();
        productService.saveProduct(productDto);
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
        boolean result = field.getText().chars().allMatch( (c) -> (c >= 48 && c <= 57) || c == 46 );
        labelToShow.setVisible(!result);

    }

    private boolean validateNotNullFields(){
        List<TextField> fieldList = List.of(txtName, txtPrice, txtStock);
        return fieldList.stream().noneMatch( (field) -> field.getText().isEmpty() );
    }

    private void setProductDto(){
        productDto.setName(txtName.getText());
        productDto.setPrice(Double.valueOf(txtPrice.getText()));
        productDto.setStock(Integer.parseInt(txtStock.getText()));
        productDto.setDescription(txtDescription.getText());
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
        WindowsManager.closeWindow(thisWindowStage);
    }

    @FXML
    private void minWindow(){
        WindowsManager.minWindow(thisWindowStage);
    }

    private Optional<ButtonType> confirmationDialog(){
        return WindowsManager.confirmDialog(productAnchorPane, "Confirmación de Datos: ¿Esta Seguro?");
    }
}
