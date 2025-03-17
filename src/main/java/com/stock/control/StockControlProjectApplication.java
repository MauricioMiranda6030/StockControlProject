package com.stock.control;

import com.stock.control.front.tools.WindowsManager;
import javafx.application.Application;
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
        WindowsManager.setContext(context);
    }

    @Override
    public void start(Stage stage) throws Exception {
        WindowsManager.openNewWindowAndKeepCurrent(WindowsManager.PATH_MAIN, "Menú");
    }

    //sale de la app
    @Override
    public void stop(){
        SpringApplication.exit(context);
    }

}
