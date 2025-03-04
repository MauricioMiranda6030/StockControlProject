package com.stock.control.front;

import com.stock.control.front.tools.SpringFXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private Button btnOpenProductsSection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            btnOpenProductsSection.setOnAction(this::openProductController);
    }

    @FXML
    private void openProductController(ActionEvent event) {
        try {
            SpringFXMLController.openNewWindowAndCloseCurrent
                    (
                            SpringFXMLController.PATH_STOCK,
                            "Control de Stock",
                            event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
