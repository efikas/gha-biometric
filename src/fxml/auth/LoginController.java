package fxml.auth;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import fxml.GhaController;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import model.Model;
import partial.AlertBox;
import partial.Partial;


public class LoginController implements Initializable{

    @FXML
    private JFXTextField userText;

    @FXML
    private JFXPasswordField passwordText;

    @FXML
    private Button loginBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
      
       loginBtn.setOnAction(e->{
           try {
                   onLogin(e);
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
               Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
           } catch (InvalidKeySpecException ex) {
               Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
           }
       });
       
    }
    
    
    
    @FXML
    private void onLogin(Event event) throws IOException, InvalidKeySpecException, SQLException {
        try {
   
            Model model = new Model();
            
            if(model.Login(userText.getText(), passwordText.getText())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Gha.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                
                GhaController controller = loader.getController();
                controller.setAuth(userText.getText(), 2);
                
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.hide();
                primaryStage.setTitle("GHA Parent");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
            else {
               AlertBox.alert("Wrong Username of Password");
               passwordText.setText("");
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
}
