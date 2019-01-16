package fxml;

//import com.jfoenix.controls.JFXButton;
//import com.jfoenix.controls.JFXDrawer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
//import javafx.event.ActionEvent;
//import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class GhaController implements Initializable {

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private AnchorPane containerPane;
    public static AnchorPane containerPanel;

    @FXML
    private JFXDrawer menuDrawer;

    
    private Parent page;
    @FXML
    private MenuItem menuDashboard;
    @FXML
    private MenuItem menuAddParent;
    @FXML
    private MenuItem menuAddPupil;
    @FXML
    private MenuItem menuHelp;
    @FXML
    private MenuItem menuClose;
    @FXML
    private MenuItem menuParentList;
    @FXML
    private MenuItem menuPupilsList;
    @FXML
    private MenuItem menuReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        containerPanel = containerPane;
        VBox menu;
        try {
//            menu = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
//            menuDrawer.setSidePane(menu);
            
            page = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            containerPane.getChildren().setAll(page);
            
        } catch (IOException ex) {
            Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Load menu actions
        menu();
        
//        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
//        transition.setRate(-1);
//        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)-> {
//            transition.setRate(transition.getRate() * -1);
//            transition.play();
//
//            if(menuDrawer.isOpened()){
//                menuDrawer.close();
//            }else{
//                menuDrawer.open();
//            }
//        });
        
    }
    
    /**
     * Stop fingerprint scanner on the dashboard page
     */
    private void stopScanner(){
        DashboardController dashBoard = new DashboardController();
        dashBoard.stop2();
    }
    
    private void menu(){
          menuAddPupil. setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuAddParent.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/Parent.fxml"));
                 containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuDashboard.setOnAction((e)->{
              try {
                  page = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuHelp.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuParentList.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/ParentList.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuPupilsList.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/PupilsList.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuReport.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuClose.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
    }
      
    
}
