/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Model;
import partial.Pupil;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //load value from database
        try {
                model.Model model = new Model();
                pupilSerial.setCellValueFactory(new PropertyValueFactory<Pupil, Integer>("id"));
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
    
}
