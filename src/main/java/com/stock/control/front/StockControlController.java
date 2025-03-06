package com.stock.control.front;

import com.stock.control.entity.Product;
import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.SpringFXMLController;
import com.stock.control.service.IProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class StockControlController implements Initializable {

    private static StockControlController instance = null;

    private StockControlController(){}

    // Patron Singleton
    private static synchronized StockControlController getInstance(){
        if(instance == null)
            instance = new StockControlController();
        return instance;
    }

    @FXML
    private AnchorPane stockControlAnchorPane;

    @Autowired
    private IProductService productService;

    @FXML
    private TableView<Product> tableProducts;

    @FXML
    private TableColumn<Product, String> description;

    @FXML
    private TableColumn<Product, Long> id;

    @FXML
    private TableColumn<Product, String> name;

    @FXML
    private TableColumn<Product, Double> price;

    @FXML
    private TableColumn<Product, Integer> stock;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnAddProduct, btnEditProduct;

    @FXML
    private ImageView btnGoBack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtBusqueda.textProperty().addListener(
                (observable, oldValue, newValue) -> searchProducts()
        );
        btnAddProduct.setOnAction(event -> openFormProduct(event, "save"));
        btnEditProduct.setOnAction(event -> openFormProduct(event, "edit"));
        btnGoBack.setOnMouseClicked(mouseEvent -> goBackToMainMenu());

        setUpColumns();
        getProducts();
    }

    @FXML
    public void setPerson(){
        ControllerManager.setProductToEdit(tableProducts.getFocusModel().getFocusedItem());
    }

    @FXML
    public void openFormProduct(ActionEvent event, String status){

        if(status.equals("edit") && ControllerManager.getProductToEdit() == null)
            ControlFXManager.buildNotification("Debe seleccionar un producto a editar", "Edición de Producto")
                    .showWarning();
        else{
                ControllerManager.setFormProductStatus(status);
                ControllerManager.setStockControlController(this);
                try {
                    SpringFXMLController.openNewWindowAndKeepCurrent(
                            SpringFXMLController.PATH_PRODUCT_FORM,
                            "Nuevo Producto"
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    private void setUpColumns(){
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));
        price.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        description.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
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
    public void searchProducts() {

        String search = txtBusqueda.getText();

        if(search.isEmpty())
            getProducts();
        else
            tableProducts.setItems(FXCollections.observableArrayList(productService.getProductsByName(search)));
    }

    @FXML
    public void goBackToMainMenu(){
        try {
            SpringFXMLController.openNewWindowAndCloseCurrent(
                    SpringFXMLController.PATH_MAIN,
                    "Menú Principal",
                    (Stage) stockControlAnchorPane.getScene().getWindow()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
