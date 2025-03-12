package com.stock.control.front;

import com.stock.control.entity.Sale;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.SpringFXMLController;
import com.stock.control.service.ISaleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
public class SalesRecordController implements Initializable {

    @Autowired
    private ISaleService saleService;

    @FXML
    private TableView<Sale> tableSales;

    @FXML
    private TableColumn<Sale, Integer> colAmount;

    @FXML
    private TableColumn<Sale, LocalDate> colDate;

    @FXML
    private TableColumn<Sale, Double> colFinalPrice;

    @FXML
    private TableColumn<Sale, Long> colId;

    @FXML
    private Button btnSaveSale;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSaveSale.setOnAction(event -> openSaleForm());

        ControllerManager.setSalesRecordController(this);

        setUpColumns();
        getSales();
    }

    private void setUpColumns(){
        colId.setCellValueFactory(new PropertyValueFactory<Sale, Long>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("totalAmount"));
        colDate.setCellValueFactory(new PropertyValueFactory<Sale, LocalDate>("dateOfSale"));
        colFinalPrice.setCellValueFactory(new PropertyValueFactory<Sale, Double>("finalPrice"));
    }

    private void resetTable(){
        ObservableList<Sale> sales = tableSales.getItems();
        sales.clear();
        tableSales.setItems(sales);
    }

    public void getSales(){
        resetTable();
        tableSales.setItems(FXCollections.observableArrayList(saleService.getAllSales()));
    }

    private void openSaleForm(){
        try {
            SpringFXMLController.openNewWindowAndKeepCurrent(
                    SpringFXMLController.PATH_SALE_FORM,
                    "Nueva Venta"
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}