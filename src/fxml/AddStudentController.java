/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.File;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Model;
import partial.Pupil;
import partial.Partial;

/**
 *
 * @author latyf
 */
public class AddStudentController implements Initializable{

    @FXML
    private ImageView pupilImage;
    @FXML
    private JFXComboBox<String> pupilClass;
    @FXML
    private JFXComboBox<String> pupilFullname;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXComboBox<String> pupilClassArm;
    @FXML
    private JFXButton btnSubmit;
    
    private ObservableList<Pupil> appMainObservableList;
    Partial partial = new Partial();
    model.Model model = new Model();
    ObservableList<String> pupilNames;
    List<Map<String, Object>> pupilsInfo;
    
    int pupilId;
    String pupilName = null;
    Blob pupilBlobImage = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateCombos();
        selectPupilCombos();    
        
        btnCancel.setOnAction(e -> {
            ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
        });
    }
    
    @FXML
    void btnAddPersonClicked(ActionEvent event) {
//        System.out.println("btnAddPersonClicked");
//        int id = Integer.valueOf(tfId.getText().trim());
//        String name = tfName.getText().trim();
//        int iAge = Integer.valueOf(tfAge.getText().trim());
//        
        Pupil data = new Pupil(this.pupilId, this.pupilName, this.pupilBlobImage);
        appMainObservableList.add(data);       
        closeStage(event);
    }
    
     private void selectPupilCombos(){
         
        pupilClass.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue observable, String oldValue, String newValue) {
                pupilClassArm.getSelectionModel().selectFirst();
                resetNameCombo();
                resetImage();
                activateSubmitBtn();
            }    
        });
         
        /**
         * 
         */
        pupilClassArm.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue observable, String oldValue, String newValue) {
                // reset the fullname combo box
                resetNameCombo();
                
                if (pupilClass.getSelectionModel().getSelectedIndex() > 0) {
                    // pupild data 
                    Map<String, Object> pupilsData = null;
                    int classIndex = pupilClass.getSelectionModel().getSelectedIndex();
                    int armIndex = pupilClassArm.getSelectionModel().getSelectedIndex();

                    try {
                       pupilsData = model.getPupils(classIndex, armIndex);
                       //get the pupils name list
                       pupilNames = (ObservableList<String>)pupilsData.get("pupilsNames"); 
                       //get the pupils information
                       pupilsInfo = (ArrayList<Map<String, Object>>)pupilsData.get("pupilsInfo");

                       pupilFullname.getItems().setAll(pupilNames);
                       
//                       if(!pupilFullname.getSelectionModel().isEmpty())
//                          pupilFullname.getSelectionModel().selectFirst();

                    } catch (Exception ex) {
                        Logger.getLogger(ParentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                resetImage();
                activateSubmitBtn();
            }    
        });
        
        /**
         * @description event of combo box for student full name
         * 
         */
        pupilFullname.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue observable, String oldValue, String newValue) {
                int index = pupilFullname.getSelectionModel().getSelectedIndex();
                //System.out.println("pupilnamelist = " + index);
                //System.out.println("pupilInfolist = " + pupilsInfo.size());
                 try {
                     if(index != 0){
                         pupilId = getPupilId(index - 1, pupilsInfo);
                         pupilName = pupilFullname.getSelectionModel().getSelectedItem().trim();
                         pupilBlobImage = setPupilImage(index - 1, pupilsInfo);
                     }
                     else{
                         pupilName = null;
                         pupilBlobImage = null;
                     }
                     activateSubmitBtn();
                 } catch (IOException ex) {
                     Logger.getLogger(AddStudentController.class.getName()).log(Level.SEVERE, null, ex);
                 }
            }    
        });
       
    }
     
    public void activateSubmitBtn(){
        if(this.pupilName == null){
            this.btnSubmit.setDisable(true);
        }
        else {
            this.btnSubmit.setDisable(false); 
        }
    }
    
    public void resetImage(){
       this.pupilBlobImage = null;
       this.pupilImage.setImage(new Image("/assets/images/user.jpg",160,180,false,true));
    }
    
     
     public void closeStage(ActionEvent event){
         ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
     }
    

    public void setAppMainObservableList(ObservableList<Pupil> tvObservableList) {
        this.appMainObservableList = tvObservableList;   
    }

    /**
     * 
     * @param selectedIndex
     * @param template
     * @return 
     */
    private int getPupilId(int selectedIndex, List<Map<String, Object>> template){
         Map<String, Object> pupilInfo = (Map<String, Object>)template.get(selectedIndex);
        return (int)pupilInfo.get("id");
    }
    
    /**
     * 
     * @param selectedIndex
     * @param template
     * @return Image 
     */
    private Blob setPupilImage(int selectedIndex, List<Map<String, Object>> template)
            throws FileNotFoundException, IOException{
       Map<String, Object> pupilInfo = (Map<String, Object>)template.get(selectedIndex);
       Blob foto = (Blob)pupilInfo.get("image");
       
       InputStream is;
       OutputStream os;
       Image imagex = null;
        try {
            is = foto.getBinaryStream();
            os = new FileOutputStream(new File("photo.jpg"));
            byte[]content = new byte[1024];
            int size = 0;
            while((size = is.read(content))!= -1)
            {
                os.write(content,0,size);
            }
            os.close();
            is.close();
            imagex = new Image("file:photo.jpg",160,180,false,true);
            pupilImage.setImage(imagex);
            
            return foto ;
        
        } catch (SQLException ex) {
            Logger.getLogger(AddStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
    
    /**
     * populate the pupil class and arm combo boxes
     */
    private void populateCombos(){
        partial.populateClass().forEach(value->{
            this.pupilClass.getItems().add(value);
        });
        partial.populateClassArm().forEach(value->{
            this.pupilClassArm.getItems().add(value);
        });
        
    }
    
    private void resetNameCombo(){
//        pupilFullname.getItems().clear();
//        pupilClassArm.getItems().add("");
//        pupilFullname.setValue("");
        //if(!pupilFullname.getSelectionModel().isEmpty())
        pupilFullname.getSelectionModel().selectFirst();
    }
    
}
