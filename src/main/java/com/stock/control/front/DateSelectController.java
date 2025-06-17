package com.stock.control.front;

import com.stock.control.front.tools.ControlFXManager;
import com.stock.control.front.tools.WindowsManager;
import com.stock.control.service.ISaleService;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class DateSelectController implements Initializable {

    @FXML
    private DatePicker datePickerFrom, datePickerTo;

    @FXML
    private AnchorPane datePickerAnchorPane;

    @Getter
    private Stage thisWindowStage;

    @Autowired
    private ISaleService saleService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> thisWindowStage = (Stage) datePickerAnchorPane.getScene().getWindow());
    }

    @FXML
    private void clientReport(){
        if (isAnyDatePickerEmpty()){
            ControlFXManager.buildNotification(
                    "Todos los Campos deben estar completos.",
                    "Advertencia"
            ).showWarning();
        }
        else if (isDateFromAfterThanDateTo()){
            ControlFXManager.buildNotification(
                    "La fecha 'Desde' no puede ser despues que la fecha 'Hasta'!",
                            "Advertencia"
                    ).showWarning();
        }
        else{
            saleService.createClientReport(datePickerFrom.getValue(), datePickerTo.getValue());
            ControlFXManager.buildNotification(
                    "/images/check.png",
                    "Reporte creado Exitosamente",
                    "Reporte creado"
            ).show();
            closeFormWithDelay();
        }
    }

    private boolean isAnyDatePickerEmpty() {
        return datePickerFrom.getValue() == null || datePickerTo.getValue() == null;
    }

    private boolean isDateFromAfterThanDateTo() {
        return datePickerFrom.getValue().isAfter(datePickerTo.getValue());
    }

    private void closeFormWithDelay() {
        PauseTransition delay = new PauseTransition(Duration.seconds(1.25));
        delay.setOnFinished(e -> closeThisForm());
        delay.play();
    }

    @FXML
    private void closeThisForm(){
        WindowsManager.closeWindow(thisWindowStage);
    }
}
