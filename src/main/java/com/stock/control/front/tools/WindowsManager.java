package com.stock.control.front.tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
* Loader para cargar los archivos FXML y los beans de Spring
* */

@Component
public class WindowsManager {

    private static final String BASE = "/com/stock/control/front/component/";
    public static final String PATH_MAIN = BASE + "main_menu.fxml";
    public static final String PATH_PRODUCT_FORM = BASE + "form_product.fxml";
    public static final String PATH_STOCK = BASE + "stock_control.fxml";
    public static final String PATH_SALE = BASE + "sales_record.fxml";
    public static final String PATH_SALE_FORM = BASE + "form_sale.fxml";
    public static final String PATH_PRODUCT_SEARCH = BASE + "product_search.fxml";

    private static final Map<String, Stage> windowsOpened = new HashMap<>();

    @Setter
    private static ApplicationContext context;

    public static Parent load(String fxmlPath) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean); //conecta con el componente con Spring

        URL fxmlLocation = WindowsManager.class.getResource(fxmlPath);
        loader.setLocation(fxmlLocation);

        return loader.load();
    }

    public static void openNewWindowAndCloseCurrent(String path, String title, Stage currentStage) throws IOException{
        if(windowsOpened.containsKey(path) && windowsOpened.get(path).isShowing()){
            windowsOpened.get(path).toFront();
        }else {
            Parent root = load(path);
            Scene scene = new Scene(root);

            Stage stage = createStage(title, scene);
            stage.show();

            currentStage.close();
            windowsOpened.put(path, stage);

            stage.setOnCloseRequest(e -> {
                windowsOpened.remove(path, stage);
            });
        }
    }

    public static void openNewWindowAndKeepCurrent(String path, String title) throws IOException{

        if(windowsOpened.containsKey(path) && windowsOpened.get(path).isShowing()){
           windowsOpened.get(path).toFront();
        }else{
            Parent root = load(path);
            Scene scene = new Scene(root);

            Stage stage = createStage(title, scene);
            stage.show();

            windowsOpened.put(path, stage);

            stage.setOnCloseRequest(event -> {
                windowsOpened.remove(path, stage);
            });
        }
    }

    private static Stage createStage(String title, Scene scene){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setScene(scene);

        return stage;
    }

    private static Optional<ButtonType> confirmationDialog(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText("Confirmar datos: ¿Está seguro?");
        alert.getDialogPane().setHeaderText(null);

        return alert.showAndWait();
    }

    public static Optional<ButtonType> confirmDialog(AnchorPane anchor, String message) {
        Stage stage = (Stage) anchor.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText(message);
        alert.setTitle("Confirmación");
        alert.getDialogPane().setHeaderText(null);

        return alert.showAndWait();
    }
}
