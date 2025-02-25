package com.stock.control.front;

import com.stock.control.entity.Product;
import com.stock.control.service.IProductService;
import javafx.fxml.FXML;
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
import org.controlsfx.control.Notifications;
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
            buildNotification("Debe haber valores en los campos obligatorios", "Campos Vacios")
                    .showError();
        else if (lblWarningPrice.isVisible() || lblWarningStock.isVisible())
            buildNotification("Debes ingresar correctamente valores númericos en Precio y Stock","Producto No guardado")
                    .showWarning();
        else{
            setProduct();
            productService.saveProduct(product);

            resetTextFields();
            buildNotification("/images/check.png", "Producto guardado correctamente", "Registro de Producto")
                    .show();
            //todo actualizar la lista de la otra ventana
        }
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

    private Notifications buildNotification(String message, String title){
        return Notifications.create()
                .text(message)
                .title(title)
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
    }

    private Notifications buildNotification(String imgPath, String message, String title){

        Image img = new Image(imgPath);
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);

        return Notifications.create()
                .text(message)
                .title(title)
                .graphic(imageView)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
    }

    @FXML
    private void closeThisForm(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
