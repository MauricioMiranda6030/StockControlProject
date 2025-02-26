package com.stock.control.front;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class ControllerManager {

    @Getter @Setter
    private static MainController mainController;

}
