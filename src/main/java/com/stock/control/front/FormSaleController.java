package com.stock.control.front;

import com.stock.control.dto.ProductDTO;
import com.stock.control.dto.SaleDTO;
import com.stock.control.entity.Sale;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.SpringFXMLController;
import com.stock.control.service.IProductService;
import com.stock.control.service.ISaleDetailsService;
import com.stock.control.service.ISaleService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class FormSaleController implements Initializable {

    @Autowired
    private ISaleService saleService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ISaleDetailsService saleDetailsService;

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

    @Getter @Setter
    StringProperty amountProperty = new SimpleStringProperty("-");
    @Getter @Setter
    StringProperty priceProperty = new SimpleStringProperty("-");
    @Getter @Setter
    StringProperty finalPriceProperty = new SimpleStringProperty("-");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblAmount.textProperty().bind(amountProperty);
        lblPrice.textProperty().bind(priceProperty);
        lblFinalPrice.textProperty().bind(finalPriceProperty);
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        listProducts.getItems().addListener((ListChangeListener<ProductDTO>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    Platform.runLater(this::updateSaleAndLabel);
                }
            }
        });

        initializeNewSale();
        ControllerManager.setFormSaleController(this);

        /*
        Creación de un listCell, a partir de la listview con los productos dto crea una list cell con el producto
        y un seleccionador de cantidad a agregar y botón para eliminar el producto
         */
        listProducts.setCellFactory(param -> new ListCell<ProductDTO>() {
            private final HBox hbox = new HBox(20);
            private final Label productLabel = new Label();
            private final TextField amountField = new TextField();
            private final Button removeButton = new Button();

            {
                removeButton.setOnAction(event -> {
                    ProductDTO productDto = getItem();
                    saleDto.getProducts().remove(productDto);
                    listProducts.setItems(FXCollections.observableArrayList(saleDto.getProducts()));
                    Platform.runLater(() -> updateSaleAndLabel());
                });

                amountField.setPrefWidth(50);
                amountField.textProperty().addListener((
                        obs, oldValue, newValue) -> {
                    ProductDTO product = getItem();
                    if (!newValue.isBlank() && product != null && !oldValue.equals(newValue)) {
                        try {
                            int amount = Integer.parseInt(newValue); //Evita valores fuera de rango

                            if (amount >= 0 && amount <= product.getStock()) {
                                product.setAmountToSell(amount);
                                Platform.runLater(() -> updateSaleAndLabel());
                            } else {
                                amountField.setText(oldValue);
                                ControlFXManager.buildNotification("¡Stock Disponible es de " + product.getStock() + "!",
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

                //armado de la row a mostrar, spacer para mandar el textfield al final.
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(productLabel, spacer, amountField, removeButton);
            }

            @Override
            protected void updateItem(ProductDTO product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setGraphic(null);
                } else {
                    productLabel.setText("Producto: " + product.getName() + "\n"
                            + "Precio: " + String.format("%.2f$", product.getPrice()) + "\n"
                            + "Stock Disponible: " + product.getStock());

                    amountField.setText("1");
                    removeButton.setText("-");
                    removeButton.setStyle("-fx-background-color: red;" +
                            "-fx-text-fill: white");

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

    private void updateSale() {
        firstPrice = calculatePrice();
        saleDto.setFinalPrice(calculateFinalPrice());
        saleDto.setTotalAmount(getTotalAmount());
    }

    private void updateLabels() {
        amountProperty.set(String.valueOf(saleDto.getTotalAmount()));
        priceProperty.set(String.format("%.2f$", firstPrice));
        finalPriceProperty.set(String.format("%.2f$", saleDto.getFinalPrice()));
    }

    private Double calculatePrice() {
        return saleDto.getProducts().stream()
                .mapToDouble(product -> product.getPrice() * product.getAmountToSell())
                .sum();
    }

    private int getTotalAmount() {
        return saleDto.getProducts().stream()
                .mapToInt(ProductDTO::getAmountToSell)
                .sum();
    }

    private Double calculateFinalPrice() {
        return calculatePrice() * 1.21;
    }

    public void addProduct(ProductDTO productDto) {

        if (saleDto.getProducts().contains(productDto))
            ControlFXManager.buildNotification("Producto ya agregadó", "Venta").showInformation();
        else {
            saleDto.getProducts().add(productDto);
            listProducts.setItems(FXCollections.observableArrayList(saleDto.getProducts()));
            Platform.runLater(this::updateSaleAndLabel);
            ControlFXManager.buildNotification(
                            "/images/check.png", "Producto Agregadó",
                            "Venta")
                    .show();
        }
    }

    @FXML
    public void saveSale() {
        saveAndUpdate();
        initializeNewSale();
        updateListsAndTables();
    }

    private void saveAndUpdate() {
        Long id = saleService.saveSale(saleDto);
        saleDetailsService.saveSaleDetails(saleDto, id);
        productService.updateStock(saleDto.getProducts());
    }

    private void updateListsAndTables() {
        ControllerManager.getProductSearchController().getProducts();
        resetProductList();
        updateSalesRecordList();
    }

    private void resetProductList() {
        ObservableList<ProductDTO> productsDto = listProducts.getItems();
        productsDto.clear();
        listProducts.setItems(productsDto);
    }

    private void updateSalesRecordList() {
        ControllerManager.getSalesRecordController().getSales();
    }

    private void initializeNewSale() {
        saleDto = new SaleDTO();
        saleDto.setProducts(new ArrayList<>());
    }

    private void updateSaleAndLabel() {
        updateSale();
        updateLabels();
    }
}
