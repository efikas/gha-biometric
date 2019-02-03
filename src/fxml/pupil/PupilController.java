/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.pupil;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import fxml.GhaController;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javax.imageio.ImageIO;
import model.pupil.PupilModel;
import org.imgscalr.Scalr;
import partial.AlertBox;
import partial.ImageProcessor;
import partial.Partial;
import partial.Validate;
import partial.ImageResizer;

/**
 *
 * @author latyf
 */
public class PupilController implements Initializable{ 
    FileChooser.ExtensionFilter imageFilter;
    private FileChooser fileChooser;
    private Image image;
    private File pupilImageFile;
    partial.Partial partial = new Partial();
    ImageResizer imageResizer = new ImageResizer();
    String resizedImage; 
    
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
    private JFXTextArea pupilComment;
    @FXML
    private Button submitBtn;
    @FXML
    private JFXComboBox<String> pupilClass;
    @FXML
    private JFXComboBox<String> pupilClassArm;
    @FXML
    private Button rotateCW;
    @FXML
    private Button rotateACW;
    
    String pupilImagemageURI = "src/assets/gen_images/pupil.jpg";
    PupilModel model = new PupilModel();
    
    private boolean updatePupil = false;
    private int updatePupilId = 0;
   

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
        
        rotateCW.setOnAction((e)->{
            try {
//                imageResizer.rotate_CW(resizedImage);
                imageResizer.rotate_CW(pupilImageFile.getPath());
                
                image = new Image(pupilImageFile.toURI().toString(), 140, 150, false, false);
                pupilImage.setImage(image);                
            } catch (IOException ex) {
                Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        rotateACW.setOnAction((e)->{
            try {
                imageResizer.rotate_CW(pupilImageFile.getPath());
                
                image = new Image(pupilImageFile.toURI().toString(), 140, 150, false, false);
                pupilImage.setImage(image);                
            } catch (IOException ex) {
                Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        submitBtn.setOnAction((e)->{
             if(updatePupil){
                 updateStudent();
             }
             else {
                 addStudent();
             }
        });
    }
    
    private void addStudent(){
        Map<String, Object> data = new HashMap<String, Object>();
            data.put("firstName", this.pupilFirstName.getText().trim().toUpperCase());
            data.put("lastName", this.pupilLastName.getText().trim().toUpperCase());
            data.put("class", this.pupilClass.getSelectionModel().getSelectedIndex());
            data.put("arm", this.pupilClassArm.getSelectionModel().getSelectedIndex());
            data.put("image", this.pupilImageFile);
            data.put("comment", this.pupilComment.getText().trim());
            
            //validate
                partial.Validate validate = new Validate();
            if (this.pupilClass.getSelectionModel().getSelectedIndex() > 0 &&
                this.pupilClassArm.getSelectionModel().getSelectedIndex() > 0 &&
                validate.validateSubmission(data)){
                    //update Map
                    data.put("middleName", this.pupilMiddleName.getText().trim().toUpperCase());
                    try {
                        model.addPupilRecord(data);

                        // clear inputs
                        clearInputs();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
                        AlertBox.alert("There was an error when saving the student record!", "Form Error", AlertType.ERROR);
                        
                    }
                
            }
            else{
                AlertBox.alert("Make Sure You fill the form correctly!", "Form Error", AlertType.ERROR);
             
            }
    }
    
    private void updateStudent(){
        Map<String, Object> data = new HashMap<String, Object>();
            data.put("firstName", this.pupilFirstName.getText().trim().toUpperCase());
            data.put("lastName", this.pupilLastName.getText().trim().toUpperCase());
            data.put("class", this.pupilClass.getSelectionModel().getSelectedIndex());
            data.put("arm", this.pupilClassArm.getSelectionModel().getSelectedIndex());
            data.put("image", this.pupilImageFile);
            data.put("comment", this.pupilComment.getText().trim());
            
            //validate
                partial.Validate validate = new Validate();
            if (this.pupilClass.getSelectionModel().getSelectedIndex() > 0 &&
                this.pupilClassArm.getSelectionModel().getSelectedIndex() > 0 &&
                validate.validateSubmission(data)){
                    //update Map
                    data.put("middleName", this.pupilMiddleName.getText().trim().toUpperCase());
                    try {
                        int response = model.updatePupilRecord(updatePupilId, data);
                        
                        if(response == 1){
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pupil/PupilsList.fxml"));
                            Parent root = loader.load();
                            PupilsListController pupilsListCont = loader.getController();
                            GhaController.containerPanel.getChildren().setAll(root);
                            pupilsListCont.loadTable();
                        }
                        else {
                            AlertBox.alert("There was an error when updating the student record!", "Form Error", AlertType.ERROR);
                        }

                    }
                    catch (FileNotFoundException ex) {
                        Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
                        AlertBox.alert("There was an error when updating the student record!", "Form Error", AlertType.ERROR);
                    }
                    catch (Exception ex) {
                       Logger.getLogger(PupilsListController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            else{
                AlertBox.alert("Make Sure You fill the form correctly!", "Form Error", AlertType.ERROR);
             
            }
    }
    
    
    private void clearInputs(){
        AlertBox.alert("Record Updated Successfully", "Upload Successfull", AlertType.INFORMATION);
        
        Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pupilFirstName.setText("");
                    pupilMiddleName.setText("");
                    pupilLastName.setText("");
                    pupilClass.getSelectionModel().clearSelection();
                    pupilClassArm.getSelectionModel().clearSelection();
                    pupilImageFile = null;
                    pupilComment.setText("");
                    Image img = new Image("/assets/images/user.jpg",160,180,false,true);
                    pupilImage.setImage(img);
                }
           });
    }
    
    private void choosePicture(MouseEvent e){
        imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);

        // get parent window
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        pupilImageFile = fileChooser.showOpenDialog(primaryStage);

        if(pupilImageFile != null){
            try {
                //write the image file into the generated image folder
                // this will be useful if the image will be rotated
                writeImage(pupilImageFile.getPath().toString());
            } catch (IOException ex) {
                Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //check file size
            if(partial.getFilesizeInKiloBytes(pupilImageFile) > 500){
                //try to resize the image to 200 by 200 if the size of image is greater than 500
                resizedImage = "src/assets/gen_images/resizedImage." + getFileExtension(pupilImageFile);
                
                try {
                    imageResizer.resize(pupilImageFile.getPath(), resizedImage, 140, 150);
                                                          
                    if(partial.getFilesizeInKiloBytes(new File(resizedImage)) > 500){
                        AlertBox.alert("Maximum Image size aloowed is 500kb");
                    }
                    else {
                        pupilImageFile = new File(resizedImage);
                        image = new Image(pupilImageFile.toURI().toString(), 140, 150, false, false);
                        pupilImage.setImage(image);
                    }
                }
                catch(Exception ex) {
                    //Todo
                    //display exception
                    Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
                    AlertBox.alert("Error Resizing Image");
                }
            }
            else{
                image = new Image(pupilImageFile.toURI().toString(), 140, 150, false, false);
                pupilImage.setImage(image);
            }
        }
    } 
    
    public void loadPupilUpdate(int pupilId){
        updatePupil = true;
        updatePupilId = pupilId;
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    loadPupil(pupilId);
                } catch (Exception ex) {
                    Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
        
    }
    
    private void loadPupil(int pupilId) throws IOException, Exception{
        Map<String, Object> pupilInfo = model.getPupil(pupilId);
        
        pupilFirstName.setText((String)pupilInfo.get("firstname"));
        pupilMiddleName.setText((String)pupilInfo.get("middlename"));
        pupilLastName.setText((String)pupilInfo.get("lastname"));
        pupilComment.setText((String)pupilInfo.get("comment"));
        
        pupilClass.getSelectionModel().select((int)pupilInfo.get("pupil_class"));
        pupilClassArm.getSelectionModel().select((int)pupilInfo.get("class_arm"));
        
        //convert the image byte to image and save in image directory
        // the load the image as file
        ImageProcessor imgProc = new ImageProcessor();
        this.pupilImageFile = new File((String)imgProc.generateImageUrl(
                (byte[])pupilInfo.get("image")
            ));
        
        pupilImage.setImage(new Image("file:" + (String)imgProc.generateImageUrl(
                (byte[])pupilInfo.get("image")
            )));
    }
    
    
    private void writeImage(String imageURI) throws FileNotFoundException, IOException{
         InputStream is = new BufferedInputStream(
                                new FileInputStream(imageURI));
        BufferedImage image = ImageIO.read(is);
        ImageIO.write(image, "jpg", new File(pupilImagemageURI));
        pupilImageFile = new File(pupilImagemageURI);
    }
    
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
    
}
