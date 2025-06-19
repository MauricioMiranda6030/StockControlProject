package com.stock.control.front;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.dto.ProductSaveDto;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.CurrencyFormater;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.IProductService;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class StockControlController implements Initializable {

    @FXML
    private AnchorPane stockControlAnchorPane;

    @Autowired
    private IProductService productService;

    @FXML
    private TableView<ProductSaveDto> tableProducts;

    @FXML
    private TableColumn<ProductSaveDto, String> description, name;

    @FXML
    private TableColumn<ProductSaveDto, ProductSaveDto> stock;

    @FXML
    private TableColumn<ProductSaveDto, Double> price;

    @FXML
    private TableColumn<ProductSaveDto, Long> id;

    @FXML
    private TableColumn<ProductSaveDto, ProductSaveDto> colAction;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnAddProduct, btnEditProduct;

    @FXML
    private ImageView btnGoBack, btnPdf;

    @FXML
    private Pane topBar;

    private Stage thisWindowStage;

    private Double x = 0d, y = 0d;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            thisWindowStage = (Stage) stockControlAnchorPane.getScene().getWindow();
            getProducts();
        });
        setUpTxtSearch();
        setUpButtons();
        setUpColumns();

        ControllerManager.setStockControlController(this);
        setMovementToTopBar();
    }

    private void setUpTxtSearch() {
        txtSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> searchProducts()
        );
    }

    private void setUpButtons() {
        btnAddProduct.setOnAction(event -> openFormProduct(event, "save"));
        btnEditProduct.setOnAction(event -> openFormProduct(event, "edit"));
        btnGoBack.setOnMouseClicked(mouseEvent -> goBackToMainMenu());
        btnPdf.setOnMouseClicked(mouseEvent -> createPdfReport());
    }

    @FXML
    private void setPerson(){
        ControllerManager.setProductToEdit(tableProducts.getFocusModel().getFocusedItem());
    }

    @FXML
    private void openFormProduct(ActionEvent event, String status){

        if(isEditAndProductNull(status))
            ControlFXManager.buildNotification(
                    "Debe seleccionar un producto a editar",
                        "Edición de Producto")
            .showWarning();
        else if (isEditAndFormIsOpen(status))
            ControlFXManager.buildNotification(
                        "Debe cerrar el Formulario de Nuevo Producto para poder Editar",
                            "Edición de Producto")
            .showWarning();
        else{
                ControllerManager.setFormProductStatus(status);
                try {
                    WindowsManager.openNewWindowAndKeepCurrent(
                            WindowsManager.PATH_PRODUCT_FORM,
                            "Nuevo Producto"
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    private static boolean isEditAndFormIsOpen(String status) {
        return status.equals("edit") && ControllerManager.getFormProductController() != null && ControllerManager.getFormProductController().getThisWindowStage().isShowing();
    }

    private static boolean isEditAndProductNull(String status) {
        return status.equals("edit") && ControllerManager.getProductToEdit() == null;
    }

    private void setUpColumns(){
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        formatPriceColumn();
        setUpColStock();
        setUpColAction();
    }

    private void setUpColStock() {
        stock.setCellValueFactory(p -> new ReadOnlyObjectWrapper<ProductSaveDto>(p.getValue()));

        stock.setCellFactory(col -> new TableCell<ProductSaveDto, ProductSaveDto>() {
            private final HBox hbox = new HBox(20);
            private final Label label = new Label();
            private final Button addButton = new Button("+");

            {
                addButton.getStyleClass().add("add-stock");
                setUpAddButton();
                buildHBox();
            }

            private void buildHBox() {
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().addAll(label, spacer, addButton);
            }

            private void setUpAddButton() {
                addButton.setOnAction(e -> {
                    ProductSaveDto productSelected = getTableView().getItems().get(getIndex());
                    ControllerManager.getProductToAddStock().setStock(productSelected.getStock());
                    ControllerManager.getProductToAddStock().setId(productSelected.getId());

                    try {
                        WindowsManager.openNewWindowAndKeepCurrent(WindowsManager.PATH_ADD_STOCK, "Agregar Stock");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            @Override
            protected void updateItem(ProductSaveDto product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setGraphic(null);
                } else {
                    label.setText(String.valueOf(product.getStock()));
                    setGraphic(hbox);
                }
            }
        });
    }

    private void formatPriceColumn() {
        price.setCellFactory(column -> new TableCell<>(){
            @Override
            protected void updateItem(Double price, boolean empty){
                super.updateItem(price, empty);
                setText(empty || price == null ? null : CurrencyFormater.getCurrency(price));
            }
        });
    }

    private void setUpColAction() {
        colAction.setCellFactory(p -> new TableCell<>(){
            private final Button btnDelete = new Button("ELIMINAR");
            {
                btnDelete.getStyleClass().add("delete-button");
                btnDelete.setOnAction(deleteProduct());
            }

            private EventHandler<ActionEvent> deleteProduct() {
                return event -> {
                    ProductSaveDto productToDelete = getTableView().getItems().get(getIndex());
                    if (isDialogOk(productToDelete)) {
                        try{
                            productService.deleteProduct(productToDelete);
                            getProducts();
                            ControlFXManager.buildNotification(
                                    "/images/check.png"
                            , "Producto Eliminado Correctamente",
                                            "Producto Eliminado")
                            .show();
                        }catch (IllegalStateException e){
                            ControlFXManager.buildNotification(
                                    "¡El Producto Tiene Ventas Relacionadas!",
                                        "No se puede eliminar el Producto")
                            .showError();
                        }
                    }
                };
            }

            private boolean isDialogOk(ProductSaveDto productToDelete) {
                return WindowsManager.confirmDialog(stockControlAnchorPane, "¿Esta Seguro de Eliminar el Producto " + productToDelete.getName() + "?").get() == ButtonType.OK;
            }

            @Override
            protected void updateItem(ProductSaveDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(btnDelete);
                    setGraphic(hBox);
                }
            }
        });
    }

    @FXML
    public void getProducts() {
        resetTable();
        tableProducts.setItems(FXCollections.observableArrayList(productService.getAllProductsDto()));
    }

    private void resetTable(){
        ObservableList<ProductSaveDto> products = tableProducts.getItems();
        products.clear();
        tableProducts.setItems(products);
    }

    @FXML
    private void searchProducts() {

        String search = txtSearch.getText();

        if(search.isEmpty())
            getProducts();
        else
            tableProducts.setItems(FXCollections.observableArrayList(productService.getProductsByNameSaveDto(search)));
    }

    @FXML
    private void goBackToMainMenu(){
        try {
            WindowsManager.openNewWindowAndCloseCurrent(
                    WindowsManager.PATH_MAIN,
                    "Menú Principal",
                    thisWindowStage
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void createPdfReport() {
        productService.createPdfReport();
        ControlFXManager.buildNotification("/images/check.png",
                "Reporte Creado Correctamente",
                "Reporte")
        .show();
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
    private void closeThisForm(){
        if(ControllerManager.getFormProductController() != null)
            WindowsManager.closeWindow(ControllerManager.getFormProductController().getThisWindowStage());
        WindowsManager.closeWindow(thisWindowStage);
        goBackToMainMenu();
    }
}
