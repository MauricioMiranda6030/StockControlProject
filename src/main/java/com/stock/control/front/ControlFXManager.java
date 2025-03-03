package com.stock.control.front;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

@Component
public class ControlFXManager {

    public static Notifications buildNotification(String message, String title){
        return Notifications.create()
                .text(message)
                .title(title)
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
    }

    public static Notifications buildNotification(String imgPath, String message, String title){
        System.out.println("a ve si mandas el mensaje digo yo no");
        return Notifications.create()
                .text(message)
                .title(title)
                .graphic(createImage(imgPath))
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
    }

    public static ImageView createImage(String imgPath){
        Image img = new Image(imgPath);
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);

        return imageView;
    }

}
