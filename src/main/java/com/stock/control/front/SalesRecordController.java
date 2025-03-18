package com.stock.control.front;

import com.stock.control.dto.SaleViewDTO;
import com.stock.control.entity.Sale;
import com.stock.control.front.tools.ControllerManager;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.ISaleDetailsService;
import com.stock.control.service.ISaleService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SalesRecordController implements Initializable {

    @Autowired
    private ISaleService saleService;

    @Autowired
    private ISaleDetailsService saleDetailsService;

    @FXML
    private AnchorPane salesAnchorPane;

    @FXML
    private TableView<SaleViewDTO> tableSales;

    @FXML
    private TableColumn<SaleViewDTO, Long> colId;

    @FXML
    private TableColumn<SaleViewDTO, String> colProducts, colFinalPrice, colDate, colAmount;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button btnSaveSale;

    @FXML
    private Pane topBar;

    private Stage thisWindowStage;

    private Double x = 0d, y = 0d;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOnActionEvents();
        ControllerManager.setSalesRecordController(this);
        setUpColumns();
        getSales();
        Platform.runLater(() -> thisWindowStage = (Stage) salesAnchorPane.getScene().getWindow());
        setMovementToTopBar();
    }

    private void setOnActionEvents() {
        btnSaveSale.setOnAction(event -> openSaleForm());
        datePicker.setOnAction(event -> filterSaleByDate());
    }

    private void setUpColumns(){
        colId.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, Long>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("totalAmount"));
        colDate.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("dateOfSale"));
        colFinalPrice.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("finalPrice"));
        colProducts.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("productsDetails"));
    }

    public void getSales(){
        resetTable();
        List<Sale> sales = saleService.getAllSales();
        buildSaleView(sales);
    }

    private void filterSaleByDate() {
        resetTable();
        if(datePicker.getValue() != null){
            List<Sale> sales = saleService.getSalesByDate(datePicker.getValue());
            buildSaleView(sales);
            tableSales.getSortOrder().add(colDate);
        }else
            getSales();
    }

    private void buildSaleView(List<Sale> sales) {
        List<SaleViewDTO> salesViewDto = sales.stream()
                .map(s -> new SaleViewDTO(s,saleDetailsService.getSaleDetailsBySaleId(s.getId())))
                .toList();
        tableSales.setItems(FXCollections.observableArrayList(salesViewDto));
    }

    private void openSaleForm(){
        try {
            WindowsManager.openNewWindowAndKeepCurrent(
                    WindowsManager.PATH_SALE_FORM,
                    "Nueva Venta"
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetTable(){
        ObservableList<SaleViewDTO> salesDto = tableSales.getItems();
        salesDto.clear();
        tableSales.setItems(salesDto);
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
        if(ControllerManager.getFormSaleController() != null)
            WindowsManager.closeWindow(ControllerManager.getFormSaleController().getThisWindowStage());
        goBackToMainMenu();
        WindowsManager.closeWindow(thisWindowStage);
    }
}