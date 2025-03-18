package com.stock.control.front;

import com.stock.control.dto.ProductViewDto;
import com.stock.control.entity.Product;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.IProductService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private TableView<ProductViewDto> tableProducts;

    @FXML
    private TableColumn<ProductViewDto, String> description, name, price, stock;

    @FXML
    private TableColumn<ProductViewDto, Long> id;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnAddProduct, btnEditProduct;

    @FXML
    private ImageView btnGoBack;

    @FXML
    private Pane topBar;

    private Stage thisWindowStage;

    private Double x = 0d, y = 0d;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTxtSearch();
        setUpButtons();
        setUpColumns();
        getProducts();
        Platform.runLater(() -> thisWindowStage = (Stage) stockControlAnchorPane.getScene().getWindow());
        setMovementToTopBar();
    }

    private void setUpTxtSearch() {
        txtBusqueda.textProperty().addListener(
                (observable, oldValue, newValue) -> searchProducts()
        );
    }

    private void setUpButtons() {
        btnAddProduct.setOnAction(event -> openFormProduct(event, "save"));
        btnEditProduct.setOnAction(event -> openFormProduct(event, "edit"));
        btnGoBack.setOnMouseClicked(mouseEvent -> goBackToMainMenu());
    }

    @FXML
    private void setPerson(){
        ControllerManager.setProductToEdit(tableProducts.getFocusModel().getFocusedItem());
    }

    @FXML
    private void openFormProduct(ActionEvent event, String status){

        if(status.equals("edit") && ControllerManager.getProductToEdit() == null)
            ControlFXManager.buildNotification("Debe seleccionar un producto a editar", "Edición de Producto")
                    .showWarning();
        else{
                ControllerManager.setFormProductStatus(status);
                ControllerManager.setStockControlController(this);
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

    private void setUpColumns(){
        name.setCellValueFactory(new PropertyValueFactory<ProductViewDto, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<ProductViewDto, Long>("id"));
        price.setCellValueFactory(new PropertyValueFactory<ProductViewDto, String>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<ProductViewDto, String>("stock"));
        description.setCellValueFactory(new PropertyValueFactory<ProductViewDto, String>("description"));
    }

    @FXML
    public void getProducts() {
        resetTable();
        tableProducts.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
    }

    private void resetTable(){
        ObservableList<Product> products = tableProducts.getItems();
        products.clear();
        tableProducts.setItems(products);
    }

    @FXML
    private void searchProducts() {

        String search = txtBusqueda.getText();

        if(search.isEmpty())
            getProducts();
        else
            tableProducts.setItems(FXCollections.observableArrayList(productService.getProductsByName(search)));
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
        WindowsManager.closeWindow(thisWindowStage);
        if(ControllerManager.getFormProductController() != null)
            WindowsManager.closeWindow(ControllerManager.getFormProductController().getThisWindowStage());
        goBackToMainMenu();
    }
}
