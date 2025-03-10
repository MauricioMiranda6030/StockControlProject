package com.stock.control;

import com.stock.control.front.tools.SpringFXMLController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

        //todo acordate cambiar esto xd
        SpringFXMLController.openNewWindowAndKeepCurrent(SpringFXMLController.PATH_SALE, "Menú");
    }

    //sale de la app
    @Override
    public void stop(){
        SpringApplication.exit(context);
    }

}
