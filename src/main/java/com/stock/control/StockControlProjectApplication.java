package com.stock.control;

import com.stock.control.front.SpringFXMLController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StockControlProjectApplication extends Application {

    @Getter
    private static ApplicationContext context;
    public static void main(String[] args) {
        launch();
    }


    //iniciar spring
    @Override
    public void init(){
        context = SpringApplication.run(StockControlProjectApplication.class);
        SpringFXMLController.setContext(context);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = SpringFXMLController.load("/com/stock/control/front/component/main.fxml");
        Scene scene = new Scene(root);

        stage.setTitle("Stock");
        stage.setScene(scene);

        stage.show();


        /*

        Inicio previo

        //settear el fxml inicial
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/stock/control/front/component/main.fxml")
        );

        //unificar controlladores de spring y javafx
        fxmlLoader.setControllerFactory(context::getBean);

        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();

        */
    }

    //sale de la app
    @Override
    public void stop(){
        SpringApplication.exit(context);
    }

}
