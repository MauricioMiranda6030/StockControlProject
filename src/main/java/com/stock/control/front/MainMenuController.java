package com.stock.control.front;

import com.stock.control.front.tools.WindowsManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Component
public class MainMenuController implements Initializable {

    @FXML
    private Pane topBar;

    private Stage thisWindowStage;

    private Double x = 0d, y = 0d;

    @FXML
    private AnchorPane mainAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> thisWindowStage = (Stage) mainAnchorPane.getScene().getWindow());
        setMovementToTopBar();
    }

    @FXML
    private void openProductController() {
        try {
            WindowsManager.openNewWindowAndCloseCurrent
                    (
                            WindowsManager.PATH_STOCK,
                            "Control de Stock",
                            (Stage) mainAnchorPane.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void openSaleRecordController() {
        try {
            WindowsManager.openNewWindowAndCloseCurrent
                    (
                            WindowsManager.PATH_SALE,
                            "Gestión de Ventas",
                            (Stage) mainAnchorPane.getScene().getWindow());
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
        if (confirmDialog())
            Platform.exit();
    }

    private boolean confirmDialog() {
        return WindowsManager.confirmDialog(mainAnchorPane, "¿Esta seguro de salir de la Aplicación?").get() == ButtonType.OK;
    }
}
