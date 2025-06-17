package com.stock.control.front.tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
* Clase para cargar los archivos FXML y los beans de Spring, dialogos de confirmación.
*/

@Component
public class WindowsManager {

    private static final String BASE = "/com/stock/control/front/component/";
    public static final String PATH_MAIN = BASE + "main_menu.fxml";
    public static final String PATH_PRODUCT_FORM = BASE + "form_product.fxml";
    public static final String PATH_STOCK = BASE + "stock_control.fxml";
    public static final String PATH_SALE = BASE + "sales_record.fxml";
    public static final String PATH_SALE_FORM = BASE + "form_sale.fxml";
    public static final String PATH_PRODUCT_SEARCH = BASE + "product_search.fxml";
    public static final String PATH_ADD_STOCK = BASE + "add_stock.fxml";
    public static final String PATH_DATE_PICKER = BASE + "date_select.fxml";

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
        if(isWindowCreatedAndIsShowing(path)){
            windowsOpened.get(path).toFront();
            windowsOpened.get(path).setIconified(false);
        }
        else {
            currentStage.close();
            OpenWindow(path, title);
        }
    }

    public static void openNewWindowAndKeepCurrent(String path, String title) throws IOException{
        if(isWindowCreatedAndIsShowing(path)){
            windowsOpened.get(path).toFront();
            windowsOpened.get(path).setIconified(false);
        }
        else
            OpenWindow(path, title);
    }

    private static void OpenWindow(String path, String title) throws IOException {
        Parent root = load(path);
        Scene scene = new Scene(root);

        Stage stage = createStage(title, scene);
        stage.show();

        windowsOpened.put(path, stage);
    }

    private static boolean isWindowCreatedAndIsShowing(String path) {
        return windowsOpened.containsKey(path) && windowsOpened.get(path).isShowing();
    }

    private static Stage createStage(String title, Scene scene){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/images/logo.png"));

        stage.setScene(scene);

        return stage;
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

    public static void moveWindow(Stage stage, MouseEvent event, Double x, Double y){
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    public static void minWindow(Stage stage){
        stage.setIconified(true);
    }

    public static void closeWindow(Stage stage){
        stage.close();
    }
}
