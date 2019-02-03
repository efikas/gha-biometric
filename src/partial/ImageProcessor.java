/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partial;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author USER
 */
public class ImageProcessor {
    
    public Image generateImage(byte[] imageBytes) throws IOException {
       if(imageBytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("src/assets/gen_images/generate_image.jpg"));
            return new Image("file:src/assets/gen_images/generate_image.jpg", 160, 180, false, false);
        }
        else {
             return new Image("/assets/images/user.jpg", 160, 180, false, false);
        }
    }
    
    public String generateImageUrl(byte[] imageBytes) throws IOException {
       if(imageBytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("src/assets/gen_images/generate_image.jpg"));
            
            return "src/assets/gen_images/generate_image.jpg";
        }
        else {
           return "src/assets/images/user.jpg";
        }
    }
    
    public Image generateImage(byte[] imageBytes, int index) throws IOException {
       if(imageBytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("src/assets/gen_images/pupil_generated_" + index + ".jpg"));
            return new Image("file:src/assets/gen_images/pupil_generated_" + index + ".jpg", 120, 160, false, false);
        }
        else {
             return new Image("/assets/images/user.jpg", 160, 180, false, false);
        }
    }
}
