package com.stock.control.front;

import com.stock.control.dto.ProductDTO;
import com.stock.control.dto.SaleDTO;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.SpringFXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

@Component
public class FormSaleController implements Initializable {

    @FXML
    private AnchorPane anchorFormSale;

    @FXML
    private Button btnGoBack, btnSave, btnSearchProduct;

    @FXML
    private Label lblAmount, lblDate, lblFinalPrice, lblPrice;

    @FXML
    private ListView<ProductDTO> listProducts;

    private SaleDTO saleDto;

    private Double firstPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listProducts.itemsProperty().addListener(
                (obs, oldValue, newValue) -> updateLabels()
        );

        lblDate.setText(LocalDate.now().toString());

        saleDto = new SaleDTO();
        saleDto.setProducts(new ArrayList<>());
        ControllerManager.setFormSaleController(this);

        listProducts.setCellFactory(param -> new ListCell<ProductDTO>(){
            private final HBox hbox = new HBox(20);
            private final Label productLabel = new Label();
            private final TextField amountField = new TextField();
            {
                amountField.setPrefWidth(50);
                amountField.textProperty().addListener((
                        obs, oldValue, newValue) ->{
                    ProductDTO product = getItem();
                    if (!newValue.isBlank()) {
                        try {
                            int amount = Integer.parseInt(newValue);
                            if (amount >= 0 && amount <= product.getStock()) {
                                product.setAmountToSell(amount);
                                updateLabels();
                            } else {
                                amountField.setText(oldValue); // Evita valores fuera de rango
                                ControlFXManager.buildNotification("No puede ingresar una cantidad mayor al stock",
                                        "¡Advertencia!")
                                .showWarning();
                            }
                        } catch (NumberFormatException e) {
                            amountField.setText(oldValue); // Evita valores no numéricos
                            ControlFXManager.buildNotification("¡Solo ingresar números!",
                                            "¡Advertencia!")
                            .showWarning();
                        }
                    }
                });

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(productLabel, spacer, amountField);
            }

            @Override
            protected void updateItem(ProductDTO product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setGraphic(null);
                } else {

                    productLabel.setText("Producto: " + product.getName() + "\n"
                    + "Precio: " + String.format("$%.2f", product.getPrice()) + "\n"
                    + "Stock Disponible: " + product.getStock());
                    amountField.setText("1");

                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    public void openProductSearch() {
        try {
            SpringFXMLController.openNewWindowAndKeepCurrent(
                    SpringFXMLController.PATH_PRODUCT_SEARCH,
                    "Busqueda de Productos"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateSale(){
        firstPrice = calculatePrice();
        saleDto.setFinalPrice(calculateFinalPrice());

    }

    private void updateLabels() {
        lblPrice.setText(calculatePrice());
        lblAmount.setText(getTotalAmount());
        lblFinalPrice.setText(calculateFinalPrice());
    }

    private Double calculatePrice() {
        return saleDto.getProducts().stream()
                        .mapToDouble(product -> product.getPrice() * product.getAmountToSell())
                        .sum();
    }

    private String getTotalAmount() {
        return String.valueOf(saleDto.getProducts().stream()
                .mapToInt(ProductDTO::getAmountToSell)
                .sum()
        );
    }

    private Double calculateFinalPrice() {
        return Double.parseDouble(lblPrice.getText()) * 1.21;
    }

    public void addProduct(ProductDTO productDto) {

        if (saleDto.getProducts().contains(productDto))
            ControlFXManager.buildNotification("Producto ya agregadó", "Venta").showInformation();
        else {
            saleDto.getProducts().add(productDto);
            listProducts.setItems(FXCollections.observableArrayList(saleDto.getProducts()));
            ControlFXManager.buildNotification(
                            "/images/check.png", "Producto Agregadó",
                            "Venta")
            .show();
        }
    }
}
