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

    
    @FXML
    private Parent page;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        containerPanel = containerPane;
        VBox menu;
        try {
            menu = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
            menuDrawer.setSidePane(menu);
            
            page = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            containerPane.getChildren().setAll(page);
            
        } catch (IOException ex) {
            Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)-> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if(menuDrawer.isOpened()){
                menuDrawer.close();
            }else{
                menuDrawer.open();
            }
        });
      
    }
    
}
