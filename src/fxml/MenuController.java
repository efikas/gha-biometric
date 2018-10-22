/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 *
 * @author latyf
 */
public class MenuController implements Initializable{
    
    private Parent page;
    
    
    @FXML
    public void onMenuClicked(ActionEvent event) throws IOException{
        JFXButton btn = (JFXButton) event.getSource();
        
        switch(btn.getAccessibleText()){
            case "dashboardSelected": 
                page = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
                GhaController.containerPanel.getChildren().setAll(page);
                break;
            case "parentSelected":
                page = FXMLLoader.load(getClass().getResource("/fxml/Parent.fxml"));
                GhaController.containerPanel.getChildren().setAll(page);
                break;
            case "pupilSelected": 
                page = FXMLLoader.load(getClass().getResource("/fxml/Pupil.fxml"));
                GhaController.containerPanel.getChildren().setAll(page);
                break;
            case "repotSelected":
                page = FXMLLoader.load(getClass().getResource("/fxml/Parent.fxml"));
                GhaController.containerPanel.getChildren().setAll(page);
                break;
            case "helpSelected": 
                page = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
                GhaController.containerPanel.getChildren().setAll(page);
                break;
        }
    }
    //private void changeColor(ActionEvent event) {
//        JFXButton btn = (JFXButton) event.getSource();
//        System.out.println(btn.getText());
//        switch(btn.getText())
//        {
//            case "Color 1":FXMLDocumentController.rootP.setStyle("-fx-background-color:#00FF00");
//                break;
//            case "Color 2":FXMLDocumentController.rootP.setStyle("-fx-background-color:#0000FF");
//                break;
//            case "Color 3":FXMLDocumentController.rootP.setStyle("-fx-background-color:#FF0000");
//                break;
//        }
    //}

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
