/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javax.swing.*;
import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.*;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.*;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import enrollment.MainForm;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javax.imageio.ImageIO;
import model.parent.ParentModel;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class DashboardController implements Initializable {
    
    @FXML
    private HBox wardHbox;
    
    @FXML
    private ImageView fPrintImage;
    
    @FXML
    private Label log;
    
    @FXML
    private Label status;
    
    private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
    private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
    private JTextField prompt = new JTextField();
    @FXML
    private ImageView parentImage;
    @FXML
    private Label labelFullname;
    @FXML
    private Label labelPhone;
    @FXML
    private Label labelRelationship;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateUI();
        //initial capturing
        init();
        start();
    } 
    
    protected void init()
    {
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
                ParentModel model = new ParentModel();
                ResultSet rs = model.fetchAllParentRecord();

                if(rs.next()){
                    // Compare the feature set with our template
                    System.out.print(rs.getString("id"));
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
                            rs.close();
                        }
                       else {
                           // show error
                       }
                   }
                }
            }
            catch(Exception ex) {
               Logger.getLogger("Verification").log(Level.SEVERE, null, ex);
//                         JOptionPane.showMessageDialog(DashboardController, ex.getLocalizedMessage(), "Fingerprint loading", JOptionPane.ERROR_MESSAGE);
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

    public void stop2() {
        try {
            capturer.stopCapture();
        }
        catch(Exception ex){}
    }

    public void setStatus(String string) {
            status.setText(string);
    }
    public void setPrompt(String string) {
            prompt.setText(string);
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

    public void updateUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
              //Creating a Stackpane 
                StackPane stackPane = new StackPane();
                //Setting the margin for the circle 
        //        stackPane.setMargin(circle, new Insets(50, 50, 50, 50));       

                //Retrieving the observable list of the Stack Pane 
                ObservableList list = stackPane.getChildren(); 
                CheckBox cb1 = new CheckBox("");
                cb1.setSelected(true);

                // TODO
                ImageView imageView = new ImageView();
                imageView.setImage(new Image("/assets/images/user.jpg",160,180,false,true));
                imageView.setFitHeight(180.0);
                imageView.setFitWidth(160.0);

                 //Adding all the nodes to the pane 
                stackPane.setAlignment(cb1,Pos.TOP_RIGHT);
                stackPane.getChildren().addAll(imageView, cb1); 

                wardHbox.setSpacing(20);
                wardHbox.setStyle("-fx-padding: 20");
                wardHbox.getChildren().add(stackPane);
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
                    generateImage(imageBytes);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       });

    }

    private void generateImage(byte[] imageBytes) throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("src/assets/gen_images/parent.jpg"));
        parentImage.setImage(new Image("file:src/assets/gen_images/parent.jpg", 160, 180, false, false));
    }

    protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose)
    {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
                return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
                return null;
        }
    }

    
}
