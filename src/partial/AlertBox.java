/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partial;

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author USER
 */
public class AlertBox {
       public static void alert(String message){
            showAlert(message);
    }
    
    public static void alert(String message, String title){
        showFullAlert(Alert.AlertType.INFORMATION, "", message);
    }
    
    public static void alert(String message, String title, Alert.AlertType alertType ){
        if(alertType != null){
            if(title != null) {
                showFullAlert(alertType, title, message);
            }
            else {
                showFullAlert(Alert.AlertType.INFORMATION, "", message);
            }
        }
        else {
            showAlert(message);
        }
    }
    
    private static void showAlert(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
              Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(message);
                Optional<ButtonType> result = alert.showAndWait();
            }
       });
    }
    
    private static void showFullAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setContentText(message);
                Optional<ButtonType> result = alert.showAndWait();
            }
       });
    } 
}
