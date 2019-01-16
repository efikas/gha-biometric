/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partial;

import java.io.File;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    
    public void alert(String message, String alertType){
        
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
