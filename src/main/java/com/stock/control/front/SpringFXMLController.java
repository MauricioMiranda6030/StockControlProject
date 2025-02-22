package com.stock.control.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/*
* Loader para cargar los archivos FXML y los beans de Spring
* */

@Component
public class SpringFXMLController {

    @Setter
    private static ApplicationContext context;

    public static Parent load(String fxmlPath) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean); //conecta con el componente con Spring

        URL fxmlLocation = SpringFXMLController.class.getResource(fxmlPath);
        loader.setLocation(fxmlLocation);

        return loader.load();
    }

    public static void openNewWindowAndCloseCurrent(String path, String title, ActionEvent event) throws IOException{
        Parent root = load(path);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    public static void openNewWindowAndKeepCurrent(String path, String title) throws IOException{
        Parent root = load(path);

        Stage stage = new Stage();
        stage.setTitle(title);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

}
