package com.stock.control.front.tools;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
* Loader para cargar los archivos FXML y los beans de Spring
* */

@Component
public class SpringFXMLController {

    public static final String PATH_MAIN = "/com/stock/control/front/component/main_menu.fxml";
    public static final String PATH_PRODUCT_FORM = "/com/stock/control/front/component/form_product.fxml";
    public static final String PATH_STOCK = "/com/stock/control/front/component/stock_control.fxml";

    private static final Map<String, Stage> windowsOpened = new HashMap<>();

    @Setter
    private static ApplicationContext context;

    public static Parent load(String fxmlPath) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean); //conecta con el componente con Spring

        URL fxmlLocation = SpringFXMLController.class.getResource(fxmlPath);
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
}
