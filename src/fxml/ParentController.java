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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import partial.Partial;
import partial.Pupil;

/**
 *
 * @author latyf
 */
public class ParentController implements Initializable{

    FileChooser.ExtensionFilter imageFilter;
    private FileChooser fileChooser;
    private Image image;
    private File file;
    partial.Partial partial = new Partial();
    private FileInputStream fis;
    
    Map<String, Integer> selectedPupils = new HashMap<String, Integer>();
    model.Model model = new Model();
    
    // List of object of pupil id and fullname base on class selected
    private List<Map<String, Integer>> pupil1FullList, pupil6FullList = new ArrayList<Map<String, Integer>>();
    
    @FXML
    private JFXTextField fullname;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXTextField mobile;
    private JFXComboBox<String> pupil1Name;
    @FXML
    private ImageView parentImage;
    @FXML
    private JFXComboBox<String> title;
    @FXML
    private ImageView rightThumbImage;
    @FXML
    private ImageView leftThumbImage;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private ImageView pupil2Image;
    @FXML
    private ImageView pupil1Image;
    @FXML
    private ImageView pupil4Image;
    @FXML
    private ImageView pupil5Image;
    @FXML
    private ImageView pupil3Image;
    @FXML
    private ImageView pupil6Image;
    
    // list of student attached ot parent record
    private List<Pupil> parendWard = new ArrayList<Pupil>();
    @FXML
    private Label pupil4NameLabel;
    @FXML
    private Label pupil1NameLabel;
    @FXML
    private Label pupil2NameLabel;
    @FXML
    private Label pupil3NameLabel;
    @FXML
    private Label pupil5NameLabel;
    @FXML
    private Label pupil6NameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // populate the pareantward list with dummy data
        // Pupil pupil = new Pupil(0, "", new Image("/assets/images/user.jpg",160,180,false,true));
        Pupil pupil = new Pupil(0, "", null);
        int i = 0;
        while(i < 5){
            this.parendWard.add(pupil);
            i++;
        }
    
       rightThumbImage.setOnMouseClicked(e -> {
           
       });
       
       leftThumbImage.setOnMouseClicked(e -> {
           
       });
       
       parentImage.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.choosePicture(e);
            }
       });
       
       // handle the selection of pupils
       selectPupil();
       
       submitBtn.setOnAction(e->{
           Map<String, Object> data = new HashMap<String, Object>();
           String parentName = this.title.getValue() + " " + this.fullname.getText();
            data.put("fullname", parentName);
            data.put("phone", this.phone.getText());
            data.put("mobile", this.mobile.getText());
            data.put("image", this.file);
//            data.put("rightThumb", this.file);
//            data.put("leftThumb", this.file);
            data.put("pupils", this.selectedPupils);
            
            try {
                model.addPupilRecord(data);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
            }
       });
       
       cancelBtn.setOnAction(e->{
       
       });
    }
    
    /**
     * 
     * @param index index of the image selected on the list
     * @param nameLabel label to display student name
     * @param imgView image view selected id
     */
    public void openDialog(int index, Label nameLabel, ImageView imgView, String imageName) {
        // observable attached with the select student dialog box
        ObservableList<Pupil> tvObservableList = FXCollections.observableArrayList();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddStudent.fxml"));
            Parent root = loader.load();
            AddStudentController controller = loader.<AddStudentController>getController();
            controller.setAppMainObservableList(tvObservableList);

            Scene scene = new Scene(root, 450, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            
           // System.out.print(tvObservableList.get(0).getName());
            this.parendWard.set(index, tvObservableList.get(0));
            nameLabel.setText(tvObservableList.get(0).getName());
            Blob img = (Blob)tvObservableList.get(0).getImage();
            setPupilImage(img, imgView, imageName);
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void pupilSelected(){
        pupil1Name.setOnAction(e -> {
            if(this.selectedPupils.containsKey("pupil1")){
                this.selectedPupils.replace("pupil1", 
                        getPupilId(this.pupil1Name.getSelectionModel().getSelectedIndex() + 1, 
                                this.pupil1FullList));
            }
            else {
                this.selectedPupils.put("pupil1",
                        getPupilId(this.pupil1Name.getSelectionModel().getSelectedIndex() + 1, 
                                this.pupil1FullList));
            }
        });
    }
    
    /**
     * 
     * @param selectedIndex
     * @param template
     * @return 
     */
    private int getPupilId(int selectedIndex, List<Map<String, Integer>> template){
        return template.get(selectedIndex).get("id");
    }
    
    private void choosePicture(MouseEvent e){
        imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);

        // get parent window
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        file = fileChooser.showOpenDialog(primaryStage);

        if(file != null){
            // check file size
            if(partial.getFilesizeInKiloBytes(file) > 500){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File Size Error");
                alert.setContentText("Maximum Image size allowed is 500kb");
                Optional<ButtonType> result = alert.showAndWait();
            }
            else{
                image = new Image(file.toURI().toString(), 160, 180, false, false);
                parentImage.setImage(image);
            }
        }
    } 
    
    /**
     * pupilImage
     */
    private void setPupilImage(Blob imageBlob, ImageView imgView, String imageName) 
            throws FileNotFoundException, IOException {
       InputStream is;
       OutputStream os;
       Image imagex = null;
       imageName = imageName + "photo.jpg";
        try {
            is = imageBlob.getBinaryStream();
            os = new FileOutputStream(new File(imageName));
            byte[]content = new byte[1024];
            int size = 0;
            while((size = is.read(content))!= -1)
            {
                os.write(content,0,size);
            }
            os.close();
            is.close();
            imagex = new Image("file:" + imageName,160,180,false,true);
            imgView.setImage(imagex);
        
        } catch (SQLException ex) {
            Logger.getLogger(AddStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void selectPupil(){
        pupil1Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(0, pupil1NameLabel, pupil1Image, "pupil1Image");
            }
       });
        pupil2Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(1,  pupil2NameLabel, pupil2Image, "pupil2Image");
            }
       });
        pupil3Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(2,  pupil3NameLabel, pupil3Image, "pupil3Image");
            }
       });
        pupil4Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(3, pupil4NameLabel, pupil4Image, "pupil4Image");
            }
       });
        pupil5Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(4, pupil5NameLabel, pupil5Image, "pupil5Image");
            }
       });
        pupil6Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(5, pupil6NameLabel, pupil6Image, "pupil6Image");
            }
       });
    }
    
}
