/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.pupil;

import fxml.GhaController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.pupil.PupilModel;
import model.models.Pupil;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class PupilsListController implements Initializable {

    @FXML
    private TableColumn pupilSerial = new TableColumn();
    @FXML
    private TableColumn pupilName = new TableColumn();
    @FXML
    private TableColumn pupilClass = new TableColumn();
    @FXML
    private TableView<Pupil> pupilsListTable  = new TableView();
    @FXML
    private TableColumn pupilClassArm  = new TableColumn();
    
    private ContextMenu contextMenu;
    private  MenuItem edit;
    private  MenuItem delete;
    private  int rowPupilId = -1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contextMenu = new ContextMenu();
        edit = new MenuItem("Edit");
        delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit, delete);
        
        loadTable();
        eventHandlers();
    } 
    
    
     public void loadTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               //load value from database
                try {
                        PupilModel model = new PupilModel();
                        //pupilSerial.setCellValueFactory(new PropertyValueFactory<Pupil, Integer>("id"));
                        pupilSerial.setCellFactory(col -> new TableCell<Pupil, Void>() {
                            @Override
                            public void updateIndex(int index) {
                                super.updateIndex(index);
                                if (isEmpty() || index < 0) {
                                    setText(null);
                                } else {
                                    setText(Integer.toString(index + 1));
                                }
                            }
                        });
                        pupilName.setCellValueFactory(new PropertyValueFactory<Pupil, String>("name"));
                        pupilClass.setCellValueFactory(new PropertyValueFactory<Pupil, String>("pupilClassName"));
                        pupilClassArm.setCellValueFactory(new PropertyValueFactory<Pupil, String>("pupilClassArmName"));
                        pupilsListTable.getItems().setAll(model.fetchAllPupils());
                        
                } catch (Exception ex) {
                    Logger.getLogger(PupilsListController.class.getName()).log(Level.SEVERE, null, ex);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText("Unable to load Pupils data from the database!");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
       });
    }
     
     private void eventHandlers(){
         pupilsListTable.setOnMouseClicked((MouseEvent event)-> {
            if(event.getButton().equals(MouseButton.SECONDARY)){
                pupilsListTable.setContextMenu(contextMenu);
                
              //get row index selected
              //pupilsListTable.getSelectionModel().selectedIndexProperty().get();
              
               rowPupilId = pupilsListTable.getSelectionModel().selectedItemProperty().getValue().getId();
            }
        });
         
         edit.setOnAction( e -> {
             try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pupil/Pupil.fxml"));
                Parent root = loader.load();
                PupilController pupilCont = loader.getController();
                GhaController.containerPanel.getChildren().setAll(root);
                pupilCont.loadPupilUpdate(rowPupilId);
                  
              } 
             catch (IOException ex) {
                  Logger.getLogger(PupilsListController.class.getName()).log(Level.SEVERE, null, ex);
              }
             catch (Exception ex) {
                Logger.getLogger(PupilsListController.class.getName()).log(Level.SEVERE, null, ex);
            }
         });
         
         delete.setOnAction( e -> {
          //TODO: delete puupil record
         });
     }
    
}
