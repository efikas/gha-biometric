package fxml;

import com.digitalpersona.onetouch.DPFPCaptureFeedback;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import model.parent.ParentModel;
import model.pupil.PupilModel;
import partial.AlertBox;
import partial.Partial;


public class GhaController implements Initializable {

    @FXML
    private AnchorPane containerPane;
    public static AnchorPane containerPanel;
    
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
    @FXML
    private AnchorPane dashboardPane;
    @FXML
    private ImageView parentImage;
    @FXML
    private Label labelFullname;
    @FXML
    private Label labelPhone;
    @FXML
    private Label labelRelationship;
    @FXML
    private ImageView fPrintImage;
    @FXML
    private Label log;
    @FXML
    private Label status;
    @FXML
    private HBox wardHbox;
    @FXML
    private ScrollPane scrollPaneContainer;
    
    private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
    private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
    @FXML
    private MenuItem menuAddUser;
    @FXML
    private MenuItem menuPassRecovery;
    @FXML
    private JFXDrawer menuDrawer;
    @FXML
    private Label userLabel;
    @FXML
    private Button btnSubmit;
    
    private ContextMenu contextMenu;
    private  MenuItem logOut;
    private  MenuItem updatePass;
    private int authLevel;
    @FXML
    private Menu menuUser;
    
    ParentModel parentModel = new ParentModel();
    PupilModel pupilModel = new PupilModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        containerPanel = containerPane;
        contextMenu = new ContextMenu();
        logOut = new MenuItem("LogOut");
        updatePass = new MenuItem("Change Password");
        contextMenu.getItems().addAll(updatePass, logOut);
        
        //Load menu actions
        menu();
        dashboardPane.setVisible(true);
        scrollPaneContainer.setVisible(false);
        
       
        //initial capturing
        init();
        start();
        
    }
    
    
    protected void init(){
            capturer.addDataListener(new DPFPDataAdapter() {
                    @Override public void dataAcquired(final DPFPDataEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {	public void run() {
                                    makeReport("The fingerprint sample was captured.");
                                    setPrompt("Scan the same fingerprint again.");
                                    process(e.getSample());
                            }});
                    }
            });
            capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
                    @Override public void readerConnected(final DPFPReaderStatusEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {	public void run() {
                                    makeReport("The fingerprint reader was connected.");
                            }});
                    }
                    @Override public void readerDisconnected(final DPFPReaderStatusEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {	public void run() {
                                    makeReport("The fingerprint reader was disconnected.");
                            }});
                    }
            });
            capturer.addSensorListener(new DPFPSensorAdapter() {
                    @Override public void fingerTouched(final DPFPSensorEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {	public void run() {
                                    makeReport("The fingerprint reader was touched.");
                            }});
                    }
                    @Override public void fingerGone(final DPFPSensorEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {	public void run() {
                                    makeReport("The finger was removed from the fingerprint reader.");
                            }});
                    }
            });
            capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
                    @Override public void onImageQuality(final DPFPImageQualityEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {	public void run() {
                                    if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
                                            makeReport("The quality of the fingerprint sample is good.");
                                    else
                                            makeReport("The quality of the fingerprint sample is poor.");
                            }});
                    }
            });
    }

    protected void process(DPFPSample sample){
        updateFPImage(sample);

        // Process the sample and create a feature set for the enrollment purpose.
        DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

        // Check quality of the sample and start verification if it's good
        if (features != null){
            try {
                //pull data from database and compare the templates
                ResultSet rs = parentModel.fetchAllParentRecord();

                if(rs.next()){
                    // Compare the feature set with our template
                    byte[] leftThumb = (byte[])rs.getBytes("leftThumb");
                    byte[] rightThumb = (byte[])rs.getBytes("rightThumb");

                    DPFPTemplate leftThumbTemplate = DPFPGlobal.getTemplateFactory().createTemplate();
                    DPFPTemplate rightThumbTemplate = DPFPGlobal.getTemplateFactory().createTemplate();
                    leftThumbTemplate.deserialize(leftThumb);
                    rightThumbTemplate.deserialize(rightThumb);

                   if(verificator.verify(features, leftThumbTemplate).isVerified()){
                       outputParentInfo(
                            rs.getString("name"), 
                            rs.getString("phone"),
                            rs.getString("relationship"),
                            rs.getBytes("image")
                       );
                       showWards(pupilModel.getParentWards(rs.getInt("id")));
                       rs.close();
                   }
                   else {
                       if(verificator.verify(features, rightThumbTemplate).isVerified()){
                            outputParentInfo(
                               rs.getString("name"), 
                               rs.getString("phone"),
                               rs.getString("relationship"),
                               rs.getBytes("image")
                            );
                            showWards(pupilModel.getParentWards(rs.getInt("id")));
                            rs.close();
                        }
                       else {
                           // show error
                           outputParentInfo(
                               "", 
                               "",
                               "",
                              null
                            );
                            rs.close();
                            
                            clearWardHBox();
                            AlertBox.alert("User Record not available!.");
                       }
                   }
                }
            }
            catch(Exception ex) {
               Logger.getLogger("Verification").log(Level.SEVERE, null, ex);
//                    JOptionPane.showMessageDialog(DashboardController, ex.getLocalizedMessage(), "Fingerprint loading", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
       

    protected void start() {
            capturer.startCapture();
            setPrompt("Using the fingerprint reader, scan your fingerprint.");
    }

    protected void stop() {
        try {
            capturer.stopCapture();
        }
        catch(Exception ex){}
    }


    public void setStatus(String string) {
        status.setText(string);
    }
    public void setPrompt(String string) {
//        prompt.setText(string);
    }
    public void makeReport(String string) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
              log.setText(string);
            }
       });
    }

    public void updateFPImage(DPFPSample sample) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
              // Draw fingerprint sample image.
                fPrintImage.setImage(convertSampleToImage(sample));
                fPrintImage.setFitHeight(120);
                fPrintImage.setFitWidth(100);
                wardHbox.setPadding(new Insets(15, 12, 15, 12));
            }
       });
    }
    
    public void clearWardHBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
              wardHbox.getChildren().clear();
            }
       });
    }

    public void updateUI(Map<String, Object> data, int index) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    //Creating a Stackpane
                    StackPane stackPane = new StackPane();
                    VBox vbox = new VBox();
                    Label wardName = new Label();
                    Label wardClass = new Label();
                    
                    wardName.setFont(Font.font(wardName.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, wardName.getFont().getSize()));
                    wardClass.setFont(Font.font(wardName.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, wardName.getFont().getSize()));
                    wardName.setText(data.get("fullname").toString());
                    wardClass.setText(data.get("pupilClass").toString());
                    
                    //Setting the margin for the circle
                    //        stackPane.setMargin(circle, new Insets(50, 50, 50, 50));
                    
                    //Retrieving the observable list of the Stack Pane
                    //ObservableList list = stackPane.getChildren();
                    CheckBox cb1 = new CheckBox("");
                    cb1.setSelected(true);
                    
                    // TODO
                    ImageView imageView = new ImageView();
                    imageView.setImage(generateImage((byte[])data.get("image"), index));
                    imageView.setFitHeight(140.0);
                    imageView.setFitWidth(120.0);
                    
                    //Adding all the nodes to the pane
                    stackPane.setAlignment(cb1,Pos.TOP_RIGHT);
                    stackPane.getChildren().addAll(imageView, cb1);
                    
                    //put the stack pane in the vertical box together with other components
                    vbox.setAlignment(Pos.CENTER);
                    vbox.getChildren().addAll(stackPane, wardName, wardClass);
                    
                    wardHbox.setSpacing(20);
                    wardHbox.setStyle("-fx-padding: 20");
                    wardHbox.getChildren().add(vbox);
                } catch (IOException ex) {
                    Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });
    }

    protected Image convertSampleToImage(DPFPSample sample) {
        BufferedImage bufferedImage = null;
        java.awt.Image img = DPFPGlobal.getSampleConversionFactory().createImage(sample);
        if (img instanceof BufferedImage) {
            bufferedImage = (BufferedImage) img;
        }
        else {
            // Create a buffered image with transparency
            bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            // Draw the image on to the buffered image
            Graphics2D bGr = bufferedImage.createGraphics();
            bGr.drawImage(img, 0, 0, null);
            bGr.dispose();
        }
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        return image;
    }

    private void outputParentInfo(String name, String phone, String rel, byte[] imageBytes){
        //run on JavaFX Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelFullname.setText(name);
                labelPhone.setText(phone);
                labelRelationship.setText(rel);
                try {
                    parentImage.setImage(generateImage(imageBytes));
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });

    }
    
    private Image generateImage(byte[] imageBytes) throws IOException {
       if(imageBytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("src/assets/gen_images/parent.jpg"));
            return new Image("file:src/assets/gen_images/parent.jpg", 160, 180, false, false);
        }
        else {
             return new Image("/assets/images/user.jpg", 160, 180, false, false);
        }
    }
    
    private Image generateImage(byte[] imageBytes, int index) throws IOException {
       if(imageBytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("src/assets/gen_images/pupil_" + index + ".jpg"));
            return new Image("file:src/assets/gen_images/pupil_" + index + ".jpg", 120, 160, false, false);
        }
        else {
             return new Image("/assets/images/user.jpg", 160, 180, false, false);
        }
    }

//    private void generateImage(byte[] imageBytes) throws IOException{
//        if(imageBytes != null) {
//            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
//            BufferedImage bImage2 = ImageIO.read(bis);
//            ImageIO.write(bImage2, "jpg", new File("src/assets/gen_images/parent.jpg"));
//            parentImage.setImage(new Image("file:src/assets/gen_images/parent.jpg", 160, 180, false, false));
//        }
//        else {
//             parentImage.setImage(new Image("/assets/images/user.jpg", 160, 180, false, false));
//        }
//    }

    protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose)
    {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
                return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
                return null;
        }
    }

    
    
    /**
     * Stop fingerprint scanner on the dashboard page
     */
    private void stopScanner(){
        dashboardPane.setVisible(false);
        scrollPaneContainer.setVisible(true);
//        DashboardController dashBoard = new DashboardController();
//        dashBoard.stop2();
    }
    
    private void showWards(List<Map<String, Object>> wardResultSet){
        
        //clear the pupil Horizontal box container before loading
        // it with the pupil information
        clearWardHBox();
//        wardResultSet.forEach(ward -> {
//             updateUI(ward, w);
//        });
        
        for(int index = 0; index < wardResultSet.size(); index++){
            updateUI(wardResultSet.get(index), index);
        }
    }
    
    private void menu(){
          menuAddPupil. setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/pupil/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuAddParent.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/parent/Parent.fxml"));
                 containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuDashboard.setOnAction((e)->{
              dashboardPane.setVisible(true);
              scrollPaneContainer.setVisible(false);
          });
          
          menuHelp.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/pupil/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuParentList.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/parent/ParentList.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuPupilsList.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/pupil/PupilsList.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuReport.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/pupil/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuClose.setOnAction((e)->{
              try {
                  stopScanner();
                  page = FXMLLoader.load(getClass().getResource("/fxml/pupil/Pupil.fxml"));
                  containerPanel.getChildren().setAll(page);
              } catch (IOException ex) {
                  Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          
          menuAddUser.setOnAction((e)->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/Register.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 450, 400);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            
            } catch (IOException ex) {
                ex.printStackTrace();
            }
          });
          
          menuPassRecovery.setOnAction((e)->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/ForgetPass.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 450, 400);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            
            } catch (IOException ex) {
                ex.printStackTrace();
            }
          });
          
          updatePass.setOnAction((e)->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/ChangePass.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 450, 400);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.showAndWait();
            
            } catch (IOException ex) {
                ex.printStackTrace();
            }
          });
          
//          userLabel.setOnMouseClicked((Event e)-> {
//              if(e.getSource().equals(MouseButton.SECONDARY)){
//                  contextMenu.show(pane, e.getTarget, e.getScreenY());
//              }
//                
//          });
          
          userLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
                // create context menu and menu items as above
              
                @Override
                public void handle(MouseEvent event) {
                    if (event.isSecondaryButtonDown()) {
                        userLabel.setContextMenu(contextMenu);
                    }
                }
            });
          
          logOut.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/Login.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        
                        Stage primaryStage = (Stage) ((Node) btnSubmit).getScene().getWindow();
                        primaryStage.setTitle("Login");
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(GhaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
    }
    
    public void setAuth(String user, int authLevel){
        userLabel.setText(user);
        this.authLevel = authLevel;
        
        if(authLevel != 1){
           menuUser.setVisible(false);
        }
    }

      
    
}
