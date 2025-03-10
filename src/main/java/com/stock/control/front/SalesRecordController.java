package com.stock.control.front;

import com.stock.control.dto.SaleDTO;
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
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SalesRecordController implements Initializable {

    @Autowired
    private ISaleService saleService;

    @FXML
    private TableView<SaleDTO> tableSales;

    @FXML
    private TableColumn<SaleDTO, Integer> colAmount;

    @FXML
    private TableColumn<SaleDTO, LocalDate> colDate;

    @FXML
    private TableColumn<SaleDTO, Double> colFinalPrice;

    @FXML
    private TableColumn<SaleDTO, Long> colId;

    @FXML
    private TableColumn<SaleDTO, List<String> > colProducts;

    @FXML
    private Button btnSaveSale;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSaveSale.setOnAction(event -> openSaleForm());

        setUpColumns();
        getSales();
    }

    private void setUpColumns(){
        colId.setCellValueFactory(new PropertyValueFactory<SaleDTO, Long>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<SaleDTO, Integer>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<SaleDTO, LocalDate>("date"));
        colFinalPrice.setCellValueFactory(new PropertyValueFactory<SaleDTO, Double>("price"));
        colProducts.setCellValueFactory(new PropertyValueFactory<SaleDTO, List<String> >("products"));
    }

    private void resetTable(){
        ObservableList<SaleDTO> sales = tableSales.getItems();
        sales.clear();
        tableSales.setItems(sales);
    }

    private void getSales(){
        resetTable();
        tableSales.setItems(FXCollections.observableArrayList(saleService.getAllDtoSales()));
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