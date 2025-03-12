package com.stock.control.front;

import com.stock.control.dto.ProductDTO;
import com.stock.control.dto.SaleDTO;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.SpringFXMLController;
import com.stock.control.service.IProductService;
import com.stock.control.service.ISaleDetailsService;
import com.stock.control.service.ISaleService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listProducts.getItems().addListener((ListChangeListener<ProductDTO>) change -> {
            updateSaleAndLabel();
        });

        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

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
                    saleDto.getProducts().remove(getItem());
                    getListView().getItems().remove(getItem());
                });

                amountField.setPrefWidth(50);
                amountField.textProperty().addListener((
                        obs, oldValue, newValue) -> {
                    ProductDTO product = getItem();
                    if (!newValue.isBlank()) {
                        try {
                            int amount = Integer.parseInt(newValue); //Evita valores fuera de rango

                            if (amount >= 0 && amount <= product.getStock()) {
                                product.setAmountToSell(amount);
                                updateSaleAndLabel();
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
        lblAmount.setText(String.valueOf(saleDto.getTotalAmount()));
        lblPrice.setText(String.format("%.2f$", firstPrice));
        lblFinalPrice.setText(String.format("%.2f$", saleDto.getFinalPrice()));
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
        return firstPrice * 1.21;
    }

    public void addProduct(ProductDTO productDto) {

        if (saleDto.getProducts().contains(productDto))
            ControlFXManager.buildNotification("Producto ya agregadó", "Venta").showInformation();
        else {
            saleDto.getProducts().add(productDto);
            listProducts.getItems().add(productDto);
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
        listProducts.getItems().clear();
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
