/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partial;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 *
 * @author latyf
 */
public class Partial {
    
    public ObservableList<String> populateClass(){
        ObservableList<String> options = 
        FXCollections.observableArrayList(
            "",
            "NUR 1",
            "NUR 2",
            "NUR 3",
            "PRY 1",
            "PRY 2",
            "PRY 3",
            "PRY 4",
            "PRY 5",
            "PRY 6"
        );
        
        return options;
    }
    
    public ObservableList<String> title(){
        ObservableList<String> options = 
        FXCollections.observableArrayList(
             "",
            "Mr",
            "Mrs",
            "Sir",
            "Mal.",
            "Alh.",
            "Hajia"
        );
        
        return options;
    }
    
    public ObservableList<String> populateClassArm(){
        ObservableList<String> options = 
        FXCollections.observableArrayList(
            "",
            "A",
            "B",
            "C",
            "TAHFEEZ"
        );
        return options;
    }
    
    public Double getFilesizeInKiloBytes(File file) {
        return (double) file.length()/1024;
    }
    
    public Boolean validate(Map<String, Object> record){
        
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            //String key = entry.getKey();
            //Object value = entry.getValue();
            
            if(entry.getValue() == null || (String) entry.getValue() == "") return false;
        }
        
        return true;
    }
    
    public static void alert(String message){
            showAlert(message);
    }
    
    public static void alert(String message, String title){
        showFullAlert(AlertType.INFORMATION, "", message);
    }
    
    public static void alert(String message, String title, AlertType alertType ){
        if(alertType != null){
            if(title != null) {
                showFullAlert(alertType, title, message);
            }
            else {
                showFullAlert(AlertType.INFORMATION, "", message);
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
              Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(message);
                Optional<ButtonType> result = alert.showAndWait();
            }
       });
    }
    
    private static void showFullAlert(AlertType alertType, String title, String message) {
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
    
    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
