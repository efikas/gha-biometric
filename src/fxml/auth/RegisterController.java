/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.auth;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import model.Model;
import partial.AlertBox;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class RegisterController implements Initializable {

    @FXML
    private JFXTextField txtFullname;
    @FXML
    private JFXTextField txtUser;
    @FXML
    private JFXPasswordField txtPass;
    @FXML
    private JFXPasswordField txtRePass;
    @FXML
    private Button btnRegister;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        btnRegister.setOnAction((e)->{
            if(Objects.equals(txtPass.getText(), txtRePass.getText())){
                try {
                    Model model = new Model();
                    boolean response = model.Register(
                                            txtFullname.getText().trim(),
                                            txtUser.getText().trim(),
                                            txtPass.getText().trim()
                                    );
                    if(response){
                        AlertBox.alert("Registeration Successful", "Successful registration", Alert.AlertType.INFORMATION);
                        txtFullname.setText("");
                        txtUser.setText("");
                        txtPass.setText("");
                        txtRePass.setText("");
                    }
                    else AlertBox.alert("There was an error when registering the user");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }    
    
}
