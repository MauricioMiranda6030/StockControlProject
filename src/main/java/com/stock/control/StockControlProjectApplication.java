package com.stock.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StockControlProjectApplication extends Application {

    public static ConfigurableApplicationContext context;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        //iniciar spring
        context = SpringApplication.run(StockControlProjectApplication.class);

        //settear el fxml inicial
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/stock/control/front/component/main.fxml")
        );

        //unificar controlladores de spring y javafx
        fxmlLoader.setControllerFactory(context::getBean);

        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }
}
