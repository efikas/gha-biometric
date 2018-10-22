/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import partial.Partial;

/**
 *
 * @author latyf
 */
public class PupilController implements Initializable{ 
    FileChooser.ExtensionFilter imageFilter;
    private FileChooser fileChooser;
    private Image image;
    private File file;
    partial.Partial partial = new Partial();
    
    private FileInputStream fis;
    @FXML
    private ImageView pupilImage;
    @FXML
    private JFXTextField pupilFirstName;
    @FXML
    private JFXTextField pupilMiddleName;
    @FXML
    private JFXTextField pupilLastName;
    @FXML
    private Button submitBtn;
    @FXML
    private JFXComboBox<String> pupilClass;
    @FXML
    private JFXComboBox<String> pupilClassArm;
   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //populate comboboxes
        partial.populateClass().forEach(value->{
            pupilClass.getItems().add(value);
        });
        partial.populateClassArm().forEach(value->{
            pupilClassArm.getItems().add(value);
        });
        
        pupilImage.setOnMouseClicked((e)-> {
            if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.choosePicture(e);
            }
        });
        
        submitBtn.setOnAction((e)->{
            Map<String, Object> data = new HashMap<String, Object>();
            
            if (this.pupilClass.getSelectionModel().getSelectedIndex() > 0 &&
                    this.pupilClassArm.getSelectionModel().getSelectedIndex() > 0){
                data.put("firstName", this.pupilFirstName.getText());
                data.put("middleName", this.pupilMiddleName.getText());
                data.put("lastName", this.pupilLastName.getText());
                data.put("class", this.pupilClass.getSelectionModel().getSelectedIndex());
                data.put("arm", this.pupilClassArm.getSelectionModel().getSelectedIndex());
                data.put("image", this.file);

                try {
                    model.Model model = new Model();
                    model.addPupilRecord(data);

                    // clear inputs
                    clearInputs();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Form Error");
                alert.setContentText("Make Sure You fill the form correctly!");
                Optional<ButtonType> result = alert.showAndWait();
            }
            
        });
    }
    
    
    private void clearInputs(){
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Upload Successfull");
        alert.setContentText("Record Updated Successfully");
        Optional<ButtonType> result = alert.showAndWait();
        
         this.pupilFirstName.setText("");
           this.pupilMiddleName.setText("");
            this.pupilLastName.setText("");
            this.pupilClass.getSelectionModel().clearSelection();
            this.pupilClassArm.getSelectionModel().clearSelection();
            this.file = null;
            Image img = new Image("../assets/images/user.jpg");
            pupilImage.setImage(img);
        
    }
    
    private void choosePicture(MouseEvent e){
        imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);

        // get parent window
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        file = fileChooser.showOpenDialog(primaryStage);

        if(file != null){
            //check file size
            if(partial.getFilesizeInKiloBytes(file) > 500){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Maximum Image size aloowed is 500kb");
//                    alert.setHeaderText("Look, a Confirmation Dialog");
//                    alert.setContentText("Are you ok with this?");
//
                Optional<ButtonType> result = alert.showAndWait();
//                    if (result.get() == ButtonType.OK){
//                    }
            }
            else{
                image = new Image(file.toURI().toString(), 142, 150, false, false);
                pupilImage.setImage(image);
            }
        }
    } 
    
}
