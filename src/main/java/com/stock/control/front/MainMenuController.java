package com.stock.control.front;

import com.stock.control.front.tools.WindowsManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Component
public class MainMenuController implements Initializable {

    private static MainMenuController instance = null;

    private MainMenuController(){}

    //Patron singleton
    private static synchronized MainMenuController getInstance(){
        if(instance == null)
            instance = new MainMenuController();
        return instance;
    }

    @FXML
    private AnchorPane mainAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}
