package com.extreme;

import javafx.scene.Node;
import javafx.stage.Window;

/**
 * Created by lemmi on 26.09.17.
 */
public class Util {

    private static double mouseX;
    private static double mouseY;

    public static void installWindowDragListener(Node node) {
        node.setOnMousePressed(evt -> {
            mouseX = evt.getScreenX();
            mouseY = evt.getScreenY();
        });

        node.setOnMouseDragged(evt -> {
            double deltaX = evt.getScreenX() - mouseX;
            double deltaY = evt.getScreenY() - mouseY;

            Window window = node.getScene().getWindow();

            window.setX(node.getScene().getWindow().getX() + deltaX);
            window.setY(node.getScene().getWindow().getY() + deltaY);

            mouseX = evt.getScreenX();
            mouseY = evt.getScreenY();
        });
    }

}
