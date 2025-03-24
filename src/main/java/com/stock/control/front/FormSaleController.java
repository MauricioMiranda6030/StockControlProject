package com.stock.control.front;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.dto.SaleDTO;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.CurrencyFormater;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.IProductService;
import com.stock.control.service.ISaleDetailsService;
import com.stock.control.service.ISaleService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Double IVA = 1.21;

    private final Logger log = LoggerFactory.getLogger(FormSaleController.class);

    @Autowired
    private ISaleService saleService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ISaleDetailsService saleDetailsService;

    @FXML
    private AnchorPane anchorFormSale;

    @FXML
    private Label lblAmount, lblDate, lblFinalPrice, lblPrice;

    @FXML
    private ListView<ProductForSaleDTO> listProducts;

    @FXML
    private Pane topBar;

    private SaleDTO saleDto;

    private Double firstPrice;

    @Getter
    private Stage thisWindowStage;

    private Double x = 0d, y = 0d;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> thisWindowStage = (Stage) anchorFormSale.getScene().getWindow());
        initializeNewSale();
        ControllerManager.setFormSaleController(this);
        createListCell();
        setUpLabels();
        moveWindowToLeft();
        setMovementToTopBar();
    }

    private void moveWindowToLeft() {
        Platform.runLater(() -> {
            Stage stage = thisWindowStage;
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            stage.setX(screenWidth * 0.1);
        });
    }

    /*
        Creación de un listCell, a partir de la listview con los productos dto crea una list cell con el producto
        y un seleccionador de cantidad a agregar y botón para eliminar el producto
    */
    private void createListCell() {
        listProducts.setCellFactory(param -> new ListCell<ProductForSaleDTO>() {
            private final HBox hbox = new HBox(20);
            private final Label productLabel = new Label();
            private final TextField amountField = new TextField();
            private final Button removeButton = new Button();

            {
                setUpRemoveButtonAction();
                buildAmountField();
                buildHBox();
            }

            private void setUpRemoveButtonAction() {
                removeButton.setOnAction(event -> {
                    deleteProductAndUpdateLabels(getItem());
                });
            }

            private void buildAmountField() {
                amountField.setPrefWidth(50);
                amountField.textProperty().addListener(
                        (obs, oldValue, newValue) -> {
                            ProductForSaleDTO product = getItem();

                            if (!newValue.isBlank() && product != null && !oldValue.equals(newValue)) {
                                manageAmountInput(oldValue, newValue, product);
                            }
                        });
            }

            private void manageAmountInput(String oldValue, String newValue, ProductForSaleDTO product) {
                try {
                    int amount = Integer.parseInt(newValue); //Evita valores fuera de rango

                    if (amount >= 0 && amount <= product.getStock()) {
                        product.setAmountToSell(amount);
                        updateSaleAndLabel();
                    } else {
                        amountField.setText(oldValue);
                        ControlFXManager.buildNotification( "!"+ newValue + " supera el Stock Disponible (" + product.getStock() + ")!",
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

            //armado de la row a mostrar, spacer para mandar el textfield al final.
            private void buildHBox() {
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(productLabel, spacer, amountField, removeButton);
            }

            @Override
            protected void updateItem(ProductForSaleDTO product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setGraphic(null);
                } else {
                    buildHBoxComponents(product);
                    setGraphic(hbox);
                }
            }

            private void buildHBoxComponents(ProductForSaleDTO product) {
                productLabel.setText(product.toString());
                amountField.setText(String.valueOf(product.getAmountToSell()));
                amountField.setAlignment(Pos.CENTER);

                removeButton.setText("❌");
                removeButton.setStyle("-fx-background-color: transparent;" +
                                        "-fx-text-fill: red;");
            }
        });
    }

    private void setUpLabels() {
        lblAmount.setText("-");
        lblPrice.setText("-");
        lblFinalPrice.setText("-");
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void deleteProductAndUpdateLabels(ProductForSaleDTO productForSaleDto) {
        saleDto.getProducts().remove(productForSaleDto);
        listProducts.setItems(FXCollections.observableArrayList(saleDto.getProducts()));
        updateSaleAndLabel();
    }

    @FXML
    public void openProductSearch() {
        try {
            WindowsManager.openNewWindowAndKeepCurrent(
                    WindowsManager.PATH_PRODUCT_SEARCH,
                    "Busqueda de Productos"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Updates y Resets
     */
    
    private void updateSale() {
        firstPrice = calculatePrice();
        saleDto.setFinalPrice(calculateFinalPrice());
        saleDto.setTotalAmount(getTotalAmount());
    }

    private void updateLabels() {
        lblAmount.setText(String.valueOf(saleDto.getTotalAmount()));
        lblPrice.setText(getCurrencyFormat(firstPrice));
        lblFinalPrice.setText(getCurrencyFormat(saleDto.getFinalPrice()));
    }

    private String getCurrencyFormat(Double price) {
        return CurrencyFormater.getCurrency(price);
    }

    private void updateListsAndTables() {
        ControllerManager.getProductSearchController().getProducts();
        resetProductList();
        updateSalesRecordList();
    }

    private void resetProductList() {
        ObservableList<ProductForSaleDTO> productsDto = listProducts.getItems();
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
    
    /*
    * Calculos de SALE
    */
    private Double calculatePrice() {
        return saleDto.getProducts().stream()
                .mapToDouble(product -> product.getPrice() * product.getAmountToSell())
                .sum();
    }

    private int getTotalAmount() {
        return saleDto.getProducts().stream()
                .mapToInt(ProductForSaleDTO::getAmountToSell)
                .sum();
    }

    private Double calculateFinalPrice() {
        return calculatePrice() * IVA;
    }
    
    /*
    * Adds y Saves
    */
    
    public void addProduct(ProductForSaleDTO productForSaleDto) {

        if (saleDto.getProducts().contains(productForSaleDto))
            ControlFXManager.buildNotification("Producto ya Agregado", "Venta").showInformation();
        else {
            saleDto.getProducts().add(productForSaleDto);
            listProducts.setItems(FXCollections.observableArrayList(saleDto.getProducts()));
            updateSaleAndLabel();
            ControlFXManager.buildNotification(
                            "/images/check.png", "Producto Agregado",
                            "Venta")
                    .show();
        }
    }

    @FXML
    private void saveSale() {
        if(saleDto.getProducts().isEmpty())
            ControlFXManager.buildNotification(
                    "¡No se han agregado Productos a la Venta!"
                            ,"¡Advertencia!")
                    .showWarning();
        else {
            if(confirmDialog("¿Esta Seguro de los Productos Elegidos?")){
                saveSaleDetailsAndUpdateStock();
                log.info("Sale just saved with a total amount of ({}) products and a final price of {}",
                        saleDto.getProducts().size(),
                        saleDto.getFinalPrice()
                );

                initializeNewSale();
                setUpLabels();
                updateListsAndTables();
                ControlFXManager.buildNotification("/images/check.png",
                                "Venta Guardada Correctamente",
                                "Venta")
                        .show();

            }
        }
    }

    private void saveSaleDetailsAndUpdateStock() {
        Long id = saleService.saveSale(saleDto);
        saleDetailsService.saveSaleDetails(saleDto, id);
        productService.updateStock(saleDto.getProducts());
    }

    private void setMovementToTopBar() {
        topBar.setOnMousePressed(event -> {
            x = event.getScreenX() - thisWindowStage.getX();
            y = event.getScreenY() - thisWindowStage.getY();
        });
        topBar.setOnMouseDragged(event -> WindowsManager.moveWindow(thisWindowStage, event, x, y));
    }

    @FXML
    private void minWindow(){
        WindowsManager.minWindow(thisWindowStage);
    }

    @FXML
    public void closeThisForm(){
        if(!saleDto.getProducts().isEmpty()){
            if(confirmDialog("¿Esta Seguro de Cancelar la Venta?")){
                closeProductSearchIfExists();
                WindowsManager.closeWindow(thisWindowStage);
            }
        }else{
            closeProductSearchIfExists();
            WindowsManager.closeWindow(thisWindowStage);
        }
    }

    private void closeProductSearchIfExists() {
        if(ControllerManager.getProductSearchController() != null)
            WindowsManager.closeWindow(ControllerManager.getProductSearchController().getThisWindowStage());
    }

    private boolean confirmDialog(String msg){
        return WindowsManager.confirmDialog(anchorFormSale, msg).get() == ButtonType.OK;
    }
}
