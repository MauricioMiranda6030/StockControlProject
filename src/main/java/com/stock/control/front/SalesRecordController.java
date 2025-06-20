package com.stock.control.front;

import com.stock.control.dto.SaleViewDTO;
import com.stock.control.front.tools.*;
import com.stock.control.service.ISaleService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
    private AnchorPane salesAnchorPane;

    @FXML
    private TableView<SaleViewDTO> tableSales;

    @FXML
    private TableColumn<SaleViewDTO, Long> colId;

    @FXML
    private TableColumn<SaleViewDTO, String> colProducts, colFinalPrice, colDate, colAmount, colClient, colCode;

    @FXML
    private TableColumn<SaleViewDTO, SaleViewDTO> colAction;

    @FXML
    private DatePicker dateFrom, dateTo;

    @FXML
    private Button btnSaveSale;

    @FXML
    private Pane topBar;

    @FXML
    private Label lblSummation, lblAmountOfSales;

    @FXML
    private TextField txtCodeFilter;

    @FXML
    private CheckBox chkExclude;

    private Stage thisWindowStage;

    private Double x = 0d, y = 0d;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            thisWindowStage = (Stage) salesAnchorPane.getScene().getWindow();
            getSales();
        });
        setOnActionEventsAndFilter();
        ControllerManager.setSalesRecordController(this);
        setUpColumns();
        setMovementToTopBar();
    }

    private void setOnActionEventsAndFilter() {
        btnSaveSale.setOnAction(event -> openSaleForm());
        dateFrom.setOnAction(event -> filterSalesByDatesCodeAndExclude());
        dateTo.setOnAction(event -> filterSalesByDatesCodeAndExclude());
        txtCodeFilter.textProperty().addListener( (obs, oldValue, newValue) -> filterSalesByDatesCodeAndExclude());
        chkExclude.setOnAction(event -> filterSalesByDatesCodeAndExclude());
    }

    private void filterSalesByDatesCodeAndExclude() {
        resetTable();
        LocalDate df = dateFrom.getValue(), dt = dateTo.getValue();
        String code = txtCodeFilter.getText();
        Boolean exclude = chkExclude.isSelected();
        setSalesViewInTable(saleService.findSalesByDatesCodeAndExclude(df, dt, code, exclude));
        updateLabels();
    }

    private void setUpColumns(){
        colId.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, Long>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("totalAmount"));
        colDate.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("dateOfSale"));
        colFinalPrice.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("finalPrice"));
        colProducts.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("productsDetails"));
        colClient.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("client"));
        colCode.setCellValueFactory(new PropertyValueFactory<SaleViewDTO, String>("code"));
        setUpColAction();
    }

    private void setUpColAction() {
        colAction.setCellFactory(s -> new TableCell<>(){
            private final Button btnDelete = new Button("ELIMINAR");
            {
                btnDelete.getStyleClass().add("delete-button");
                btnDelete.setOnAction(deleteSale());
            }

            private EventHandler<ActionEvent> deleteSale() {
                return event -> {
                    SaleViewDTO saleToDelete = getTableView().getItems().get(getIndex());
                    if (isDialogOk()) {
                        try{
                            saleService.deleteSaleById(saleToDelete.getId());
                            getSales();
                        }catch (IllegalStateException e){
                            ControlFXManager.buildNotification(
                                            "¡El Producto Tiene Ventas Relacionadas!",
                                            "No se puede eliminar el Producto")
                                    .showError();
                        }
                    }
                };
            }

            private boolean isDialogOk() {
                return WindowsManager.confirmDialog(salesAnchorPane, "¿Esta Seguro de Eliminar la Venta?").get() == ButtonType.OK;
            }

            @Override
            protected void updateItem(SaleViewDTO item, boolean empty) {
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

    public void getSales(){
        resetTable();
        List<SaleViewDTO> salesDto = saleService.getAllSalesViewDto();
        setSalesViewInTable(salesDto);
        updateLabels();
    }

    private void setSalesViewInTable(List<SaleViewDTO> sales){
        tableSales.setItems(FXCollections.observableArrayList(sales));
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

    private void updateLabels(){
        updateAmountLabel();
        updateSummation();
    }

    private void updateAmountLabel(){
        lblAmountOfSales.setText(getAmountOfSales().toString());
    }

    private Integer getAmountOfSales(){
        return tableSales.getItems().size();
    }

    private void updateSummation(){
        lblSummation.setText(CurrencyFormater.getCurrency(calculateSummation()));
    }

    private Double calculateSummation(){
        return tableSales.getItems().stream().mapToDouble(
                s -> Double.parseDouble(CleanFormat.cleanPrice(s.getFinalPrice())))
                .sum();
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

    @FXML
    private void resetDatePicker(){
        dateFrom.setValue(null);
        dateTo.setValue(null);
    }

    @FXML
    private void tableToPdf(){
        saleService.createPdfReport(
                tableSales.getItems(),
                lblAmountOfSales.getText(),
                lblSummation.getText()
        );

        ControlFXManager.buildNotification(
                "/images/check.png",
                "Tabla a PDF Creado",
                "Reporte Creado"
                ).show();
    }

    @FXML
    private void openDatePickerWindow(){
        try {
            WindowsManager.openNewWindowAndKeepCurrent(WindowsManager.PATH_DATE_PICKER, "Selección de Fecha");
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
            if(ControllerManager.getFormSaleController().getThisWindowStage().isShowing())
                ControllerManager.getFormSaleController().closeThisForm();
        goBackToMainMenu();
        WindowsManager.closeWindow(thisWindowStage);
    }
}