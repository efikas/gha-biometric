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
            "Nur 1",
            "Nur 2",
            "Nur 3",
            "Pry 1",
            "Pry 2",
            "Pry 3",
            "Pry 4",
            "Pry 5",
            "Pry 6"
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
            "Mallam"
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
            "D",
            "E",
            "F"
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
}
