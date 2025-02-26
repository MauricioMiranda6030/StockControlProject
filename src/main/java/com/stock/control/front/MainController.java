package com.stock.control.front;

import com.stock.control.entity.Product;
import com.stock.control.service.IProductService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainController implements Initializable {

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
    private Button btnAddProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtBusqueda.textProperty().addListener(
                (observable, oldValue, newValue) -> searchProducts()
        );

        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        id.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));
        price.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        description.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));

        getProducts();
    }

    @FXML
    public void openFormProduct(ActionEvent event) throws IOException{
        SpringFXMLController.openNewWindowAndKeepCurrent(
                "/com/stock/control/front/component/form_product.fxml",
                "Nuevo Producto"
        );

        ControllerManager.setMainController(this);
    }

    @FXML
    public void getProducts() {
        tableProducts.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
    }

    @FXML
    public void searchProducts() {

        String search = txtBusqueda.getText();

        if(search.isEmpty())
            getProducts();
        else
            tableProducts.setItems(FXCollections.observableArrayList(productService.getProductsByName(search)));
    }
}
