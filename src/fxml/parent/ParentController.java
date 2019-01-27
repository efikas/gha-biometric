/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.parent;

import com.digitalpersona.onetouch.DPFPTemplate;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import enrollment.EnrollmentForm;
//import static enrollment.MainForm.TEMPLATE_PROPERTY;
import enrollment.VerificationForm;
import fxml.pupil.AddStudentController;
import fxml.pupil.PupilController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import javax.swing.*;
import model.Model;
import partial.Partial;
import model.models.Pupil;
import partial.Validate;

/**
 *
 * @author latyf
 */
public class ParentController extends JFrame implements Initializable{

    FileChooser.ExtensionFilter imageFilter;
    private FileChooser fileChooser;
    private Image image;
    private File file = null;
    partial.Partial partial = new Partial();
    private FileInputStream fis;
    private byte[] leftThumbStream, rightThumbStream = null;
    
    //toggle to know the thumb selected
    // 0 - none, 1 - Left, 2 - Right
    int thumbToggle = 0;
    
    //enrollment
    public static String TEMPLATE_PROPERTY = "template";
    private DPFPTemplate template;
    
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
//    private Button cancelBtn;
//    @FXML
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
        // populate title
        partial.title().forEach(value->{
            this.title.getItems().add(value);
        });
        
        // populate the pareantward list with dummy data
        // Pupil pupil = new Pupil(0, "", new Image("/assets/images/user.jpg",160,180,false,true));
        Pupil pupil = new Pupil(0, "", null);
        int i = 0;
        while(i < 6){
            this.parendWard.add(null);
            i++;
        }
    
       rightThumbImage.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
               thumbToggle = 2;
               onEnroll("Right ");
            }
       });
       
       leftThumbImage.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
               thumbToggle = 1;
               onEnroll("Left ");
            }
       });
       
       parentImage.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.choosePicture(e);
            }
       });
       
       // handle the selection of pupils
       selectPupil();
       setTemplate(null);
       
       submitBtn.setOnAction(e->{
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("fullname", this.fullname.getText());
            data.put("phone", this.phone.getText());
            data.put("title", this.title.getValue());
            data.put("image", this.file);
            data.put("rightThumb", this.rightThumbStream);
            data.put("leftThumb", this.leftThumbStream);
           //validate
            partial.Validate validate = new Validate();
           if(validate.validateSubmission(data)){
               //update Map
                data.remove("title");
                
                String parentName = this.title.getValue() + " " + this.fullname.getText();
                data.replace("fullname", parentName);
                data.put("mobile", this.mobile.getText());
                data.put("pupils", this.parendWard);
//               System.out.println(this.parendWard.get(0).getName());

                 try {
                     model.addParentRecord(data);
                 } catch (FileNotFoundException ex) {
                     Logger.getLogger(PupilController.class.getName()).log(Level.SEVERE, null, ex);
                 }
           }
           else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation");
                alert.setHeaderText("Fill the form correclty");
                Optional<ButtonType> result = alert.showAndWait();
           }
       });
       
//       cancelBtn.setOnAction(e->{
//       
//       });
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
            
//            System.out.print(tvObservableList);
            if(tvObservableList.size() > 0) {
                this.parendWard.set(index, tvObservableList.get(0));
                nameLabel.setText(tvObservableList.get(0).getName());
                Blob img = (Blob)tvObservableList.get(0).getImage();
                setPupilImage(img, imgView, imageName);
            }
            
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
       imageName = "src/assets/gen_images/" + imageName + ".jpg";
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
                this.openDialog(0, pupil1NameLabel, pupil1Image, "pupil_1");
            }
       });
        pupil2Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(1,  pupil2NameLabel, pupil2Image, "pupil_2");
            }
       });
        pupil3Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(2,  pupil3NameLabel, pupil3Image, "pupil_3");
            }
       });
        pupil4Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(3, pupil4NameLabel, pupil4Image, "pupil_4");
            }
       });
        pupil5Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(4, pupil5NameLabel, pupil5Image, "pupil_5");
            }
       });
        pupil6Image.setOnMouseClicked(e -> {
           if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
                this.openDialog(5, pupil6NameLabel, pupil6Image, "pupil_6");
            }
       });
    }
    
    private void onEnroll(String thumb) {
        EnrollmentForm form = new EnrollmentForm(this, thumb);
        form.setVisible(true);
    }

    private void onVerify() {
        VerificationForm form = new VerificationForm(this);
        form.setVisible(true);
    }

     public DPFPTemplate getTemplate() {
            return template;
    }
    public void setTemplate(DPFPTemplate template) {
            DPFPTemplate old = this.template;
            this.template = template;
            firePropertyChange(TEMPLATE_PROPERTY, old, template);
            
            //convert the template to stream 
            try {
                if(thumbToggle == 1 && template != null){
                    leftThumbStream = template.serialize();
                    leftThumbImage.setImage(new Image("file:src/assets/images/success_print.png"));
//                            new FileOutputStream(file);
//                    leftThumbStream.write(template.serialize());
//                    leftThumbStream.close();
                    
                    //confirm left thumb scan status
                }
                
                if(thumbToggle == 2 && template != null){
                    rightThumbStream = template.serialize();
                    rightThumbImage.setImage(new Image("file:src/assets/images/success_print.png"));
//                            new FileOutputStream(file);
//                    rightThumbStream.write(template.serialize());
//                    rightThumbStream.close();
                    
                    //confirm right thumb scan status
                }
                else {
                    
                }
            } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Fingerprint serialization error", JOptionPane.ERROR_MESSAGE);
            }
    }
    
//    private boolean validateSubmission(){
//        if(
//               this.title.getValue() !=  ""  && 
//                this.fullname.getText() != "" && 
//                 this.phone.getText() != "" && 
//                 this.file != null && 
//                 this.rightThumbStream != null && 
//                 this.leftThumbStream != null
//            ) {
//            return true;
//        }
//        return false;
//    }
//    
//    private void onSave() {
//        while (true) {
//            try {
//                    String dir ="src\\\\fprints\\";
//                    File file = new File(dir + file.toString() + ".fpt");
//                    if (file.exists()) {
//                        int choice = JOptionPane.showConfirmDialog(this,
//                                String.format("File \"%1$s\" already exists.\nDo you want to replace it?", file.toString()),
//                                "Fingerprint saving", 
//                                JOptionPane.YES_NO_CANCEL_OPTION);
//                        if (choice == JOptionPane.NO_OPTION)
//                                continue;
//                        else if (choice == JOptionPane.CANCEL_OPTION)
//                                break;
//                    }
//                    FileOutputStream stream = new FileOutputStream(file);
//                    stream.write(getTemplate().serialize());
//                    stream.close();
//            } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Fingerprint saving", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
    
}
